package net.minecraftforge.eventbus.api;

/**
 * Base class for all events posted on an {@link IEventBus}. Mirrors the small subset of Forge's
 * {@code net.minecraftforge.eventbus.api.Event} that the ported mod relies on.
 */
public class Event {
	private boolean canceled = false;

	public Event() {
	}

	/**
	 * @return {@code true} if this event may be cancelled. Overridden by cancellable subclasses.
	 */
	public boolean isCancelable() {
		return true;
	}

	public boolean isCanceled() {
		return this.canceled;
	}

	public void setCanceled(boolean cancel) {
		this.canceled = cancel;
	}

	/**
	 * Result of an event, kept for source compatibility with generated code.
	 */
	public enum Result {
		DENY,
		DEFAULT,
		ALLOW
	}
}
