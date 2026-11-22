package net.minecraftforge.event;

import net.minecraftforge.eventbus.api.Event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Game-loop tick events, mirroring Forge's {@code TickEvent} hierarchy. The ported mod posts and
 * reads {@link ServerTickEvent} together with the {@link Phase} it carries.
 */
public class TickEvent extends Event {
	public enum Phase {
		START,
		END
	}

	public enum Type {
		WORLD,
		PLAYER,
		CLIENT,
		SERVER,
		RENDER,
		LEVEL
	}

	public final Type type;
	public final Phase phase;

	public TickEvent(Type type, Phase phase) {
		this.type = type;
		this.phase = phase;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}

	public static class ServerTickEvent extends TickEvent {
		public ServerTickEvent(Phase phase) {
			super(Type.SERVER, phase);
		}
	}

	public static class ClientTickEvent extends TickEvent {
		public ClientTickEvent(Phase phase) {
			super(Type.CLIENT, phase);
		}
	}

	public static class LevelTickEvent extends TickEvent {
		public final Level level;

		public LevelTickEvent(Phase phase, Level level) {
			super(Type.LEVEL, phase);
			this.level = level;
		}
	}

	public static class PlayerTickEvent extends TickEvent {
		public final Player player;

		public PlayerTickEvent(Phase phase, Player player) {
			super(Type.PLAYER, phase);
			this.player = player;
		}
	}
}
