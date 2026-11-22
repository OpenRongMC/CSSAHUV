package net.minecraftforge.network.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public final class SimpleChannel {
	private static final List<MessageBinding<?>> CLIENT_PENDING = new ArrayList<>();

	private final Identifier channelName;
	private final Map<Class<?>, MessageBinding<?>> bindings = new HashMap<>();

	public SimpleChannel(Identifier channelName) {
		this.channelName = channelName;
	}

	public <MSG> void registerMessage(int index, Class<MSG> type, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder,
			BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer) {
		Identifier payloadId = Identifier.fromNamespaceAndPath(this.channelName.getNamespace(), this.channelName.getPath() + "/" + index);
		MessageBinding<MSG> binding = new MessageBinding<>(payloadId, type, encoder, decoder, consumer);
		this.bindings.put(type, binding);

		PayloadTypeRegistry.serverboundPlay().register(binding.payloadType, binding.codec);
		PayloadTypeRegistry.clientboundPlay().register(binding.payloadType, binding.codec);

		ServerPlayNetworking.registerGlobalReceiver(binding.payloadType, (payload, context) -> {
			MSG message = payload.message();
			binding.consumer.accept(message, () -> new NetworkEvent.Context(context.player(), false));
		});

		// Client receivers are installed from SchoolModClient after common setup.
		CLIENT_PENDING.add(binding);
	}

	/** Call from client entrypoint after common setup messages are registered. */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void registerClientReceivers() {
		for (MessageBinding<?> binding : CLIENT_PENDING) {
			registerClientReceiver((MessageBinding) binding);
		}
	}

	private static <MSG> void registerClientReceiver(MessageBinding<MSG> binding) {
		// Touch SimpleChannelClient only on the client path so dedicated servers never resolve it.
		SimpleChannelClient.registerGlobalReceiver(binding.payloadType, (payload, context) -> {
			MSG message = payload.message();
			binding.consumer.accept(message, () -> new NetworkEvent.Context(null, true));
		});
	}

	@SuppressWarnings("unchecked")
	public <MSG> void sendToServer(MSG message) {
		MessageBinding<MSG> binding = (MessageBinding<MSG>) this.bindings.get(message.getClass());
		if (binding == null) {
			throw new IllegalStateException("Unregistered message " + message.getClass());
		}
		SimpleChannelClient.send(new Payload<>(binding.payloadType, message));
	}

	public <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
		if (target != null && target.player() != null) {
			send(message, target.player());
		} else if (target != null && target.broadcast()) {
			net.minecraft.server.MinecraftServer server = net.xbtstudio.school.SchoolMod.getServer();
			if (server == null) {
				return;
			}
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				send(message, player);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <MSG> void send(MSG message, ServerPlayer player) {
		MessageBinding<MSG> binding = (MessageBinding<MSG>) this.bindings.get(message.getClass());
		if (binding == null) {
			throw new IllegalStateException("Unregistered message " + message.getClass());
		}
		ServerPlayNetworking.send(player, new Payload<>(binding.payloadType, message));
	}

	private record MessageBinding<MSG>(CustomPacketPayload.Type<Payload<MSG>> payloadType, Class<MSG> messageType,
			BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder,
			BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer, StreamCodec<RegistryFriendlyByteBuf, Payload<MSG>> codec) {
		MessageBinding(Identifier id, Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder,
				BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer) {
			this(new CustomPacketPayload.Type<>(id), messageType, encoder, decoder, consumer,
					StreamCodec.of((buf, payload) -> encoder.accept(payload.message(), buf),
							buf -> new Payload<>(new CustomPacketPayload.Type<>(id), decoder.apply(buf))));
		}
	}

	private record Payload<MSG>(CustomPacketPayload.Type<Payload<MSG>> payloadType, MSG message) implements CustomPacketPayload {
		@Override
		public Type<? extends CustomPacketPayload> type() {
			return this.payloadType;
		}
	}
}
