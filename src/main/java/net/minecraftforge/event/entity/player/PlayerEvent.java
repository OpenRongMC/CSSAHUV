package net.minecraftforge.event.entity.player;

import net.minecraftforge.eventbus.api.Event;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Player-related events, mirroring the Forge {@code PlayerEvent} subtypes used by the ported mod.
 * {@link #getEntity()} returns the player the event concerns.
 */
public class PlayerEvent extends Event {
	private final Player entity;

	public PlayerEvent(Player player) {
		this.entity = player;
	}

	public Player getEntity() {
		return this.entity;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}

	public static class PlayerLoggedInEvent extends PlayerEvent {
		public PlayerLoggedInEvent(Player player) {
			super(player);
		}
	}

	public static class PlayerLoggedOutEvent extends PlayerEvent {
		public PlayerLoggedOutEvent(Player player) {
			super(player);
		}
	}

	public static class PlayerRespawnEvent extends PlayerEvent {
		private final boolean endConquered;

		public PlayerRespawnEvent(Player player, boolean endConquered) {
			super(player);
			this.endConquered = endConquered;
		}

		public boolean isEndConquered() {
			return this.endConquered;
		}
	}

	public static class PlayerChangedDimensionEvent extends PlayerEvent {
		private final ResourceKey<Level> from;
		private final ResourceKey<Level> to;

		public PlayerChangedDimensionEvent(Player player, ResourceKey<Level> from, ResourceKey<Level> to) {
			super(player);
			this.from = from;
			this.to = to;
		}

		public ResourceKey<Level> getFrom() {
			return this.from;
		}

		public ResourceKey<Level> getTo() {
			return this.to;
		}
	}

	public static class Clone extends PlayerEvent {
		private final Player original;
		private final boolean wasDeath;

		public Clone(Player original, Player newPlayer, boolean wasDeath) {
			super(newPlayer);
			this.original = original;
			this.wasDeath = wasDeath;
		}

		public Player getOriginal() {
			return this.original;
		}

		public boolean isWasDeath() {
			return this.wasDeath;
		}
	}
}
