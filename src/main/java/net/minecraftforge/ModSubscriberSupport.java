package net.minecraftforge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ModSubscriberSupport {
	private ModSubscriberSupport() {
	}

	public static void registerAnnotated(Class<?> clazz) {
		Mod.EventBusSubscriber ann = clazz.getAnnotation(Mod.EventBusSubscriber.class);
		if (ann == null) {
			return;
		}
		if (ann.bus() == Mod.EventBusSubscriber.Bus.MOD) {
			FMLJavaModLoadingContext.get().getModEventBus().register(clazz);
		} else {
			MinecraftForge.EVENT_BUS.register(clazz);
		}
	}
}
