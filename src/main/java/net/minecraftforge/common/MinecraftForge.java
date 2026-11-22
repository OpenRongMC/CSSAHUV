package net.minecraftforge.common;

import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Holder of the global FORGE event bus (runtime/game events), mirroring
 * {@code net.minecraftforge.common.MinecraftForge}.
 */
public final class MinecraftForge {
	/** The FORGE bus: runtime events such as ticks, player events and command registration. */
	public static final IEventBus EVENT_BUS = new IEventBus();

	private MinecraftForge() {
	}
}
