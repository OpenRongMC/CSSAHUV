package net.minecraftforge.network;

/**
 * Namespace for Forge's built-in play messages. Only {@link SpawnEntity} is referenced by the
 * ported mod &mdash; as a constructor parameter type on custom entities &mdash; so it is provided
 * as an empty stub.
 */
public final class PlayMessages {
	private PlayMessages() {
	}

	/** Empty stand-in for Forge's entity-spawn message type. */
	public static final class SpawnEntity {
		public SpawnEntity() {
		}
	}
}
