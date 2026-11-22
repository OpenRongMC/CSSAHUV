package net.minecraftforge.network;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketDistributor<T> {
	public static final PacketDistributor<ServerPlayer> PLAYER = new PacketDistributor<>(false);
	public static final PacketDistributor<ResourceKey<Level>> DIMENSION = new PacketDistributor<>(true);
	public static final PacketDistributor<Void> ALL = new PacketDistributor<>(true);

	private final boolean broadcast;

	private PacketDistributor(boolean broadcast) {
		this.broadcast = broadcast;
	}

	public PacketTarget with(Supplier<T> supplier) {
		ServerPlayer player = null;
		if (!this.broadcast && supplier != null) {
			Object value = supplier.get();
			if (value instanceof ServerPlayer serverPlayer) {
				player = serverPlayer;
			}
		}
		return new PacketTarget(player, this.broadcast);
	}

	public PacketTarget noArg() {
		return new PacketTarget(null, this.broadcast);
	}

	public static final class PacketTarget {
		private final ServerPlayer player;
		private final boolean broadcast;

		PacketTarget(ServerPlayer player, boolean broadcast) {
			this.player = player;
			this.broadcast = broadcast;
		}

		public ServerPlayer player() {
			return this.player;
		}

		public boolean broadcast() {
			return this.broadcast;
		}
	}

	public static void sendToServer(SimpleChannel channel, Object message) {
		channel.sendToServer(message);
	}
}
