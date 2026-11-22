package net.minecraftforge.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class NetworkEvent {
	private NetworkEvent() {
	}

	public static final class Context {
		private final Player sender;
		private final boolean clientSide;

		public Context(Player sender, boolean clientSide) {
			this.sender = sender;
			this.clientSide = clientSide;
		}

		public void enqueueWork(Runnable runnable) {
			runnable.run();
		}

		public void setPacketHandled(boolean handled) {
		}

		public ServerPlayer getSender() {
			return this.sender instanceof ServerPlayer serverPlayer ? serverPlayer : null;
		}

		public boolean isClientSide() {
			return this.clientSide;
		}

		public NetworkDirection getDirection() {
			return this.clientSide ? NetworkDirection.PLAY_TO_CLIENT : NetworkDirection.PLAY_TO_SERVER;
		}
	}

	public enum LogicalSide {
		CLIENT,
		SERVER;

		public boolean isClient() {
			return this == CLIENT;
		}

		public boolean isServer() {
			return this == SERVER;
		}
	}

	public enum NetworkDirection {
		PLAY_TO_SERVER(LogicalSide.SERVER),
		PLAY_TO_CLIENT(LogicalSide.CLIENT);

		private final LogicalSide receptionSide;

		NetworkDirection(LogicalSide receptionSide) {
			this.receptionSide = receptionSide;
		}

		public LogicalSide getReceptionSide() {
			return this.receptionSide;
		}
	}
}
