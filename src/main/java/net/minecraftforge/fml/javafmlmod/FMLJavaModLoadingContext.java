package net.minecraftforge.fml.javafmlmod;

import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Singleton providing access to the mod (MOD) event bus, mirroring Forge's
 * {@code FMLJavaModLoadingContext}. The MOD bus is distinct from
 * {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS} (the FORGE bus).
 */
public final class FMLJavaModLoadingContext {
	private static final FMLJavaModLoadingContext INSTANCE = new FMLJavaModLoadingContext();

	private final IEventBus modEventBus = new IEventBus();

	private FMLJavaModLoadingContext() {
	}

	public static FMLJavaModLoadingContext get() {
		return INSTANCE;
	}

	public IEventBus getModEventBus() {
		return this.modEventBus;
	}
}
