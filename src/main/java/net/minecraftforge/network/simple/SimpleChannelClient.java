package net.minecraftforge.network.simple;

import java.util.function.BiConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Client-only networking helpers. Kept in a separate class so dedicated servers never
 * resolve {@link ClientPlayNetworking} / {@code LocalPlayer} via {@link SimpleChannel}'s
 * constant pool.
 */
@Environment(EnvType.CLIENT)
public final class SimpleChannelClient {
	private SimpleChannelClient() {
	}

	public static <T extends CustomPacketPayload> void registerGlobalReceiver(CustomPacketPayload.Type<T> type,
			BiConsumer<T, ClientPlayNetworking.Context> handler) {
		ClientPlayNetworking.registerGlobalReceiver(type, handler::accept);
	}

	public static void send(CustomPacketPayload payload) {
		ClientPlayNetworking.send(payload);
	}
}
