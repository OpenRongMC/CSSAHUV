package net.minecraftforge.fml.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.api.distmarker.Dist;

/**
 * Marks the main mod class. Mirrors Forge's {@code @Mod} plus the nested
 * {@code @Mod.EventBusSubscriber} used for automatic listener registration.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
	String value();

	/**
	 * Classes annotated with this will have their {@code static}
	 * {@link net.minecraftforge.eventbus.api.SubscribeEvent} methods registered automatically
	 * onto the bus named by {@link #bus()}.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface EventBusSubscriber {
		enum Bus {
			/** Registration / lifecycle bus (the mod event bus). */
			MOD,
			/** Runtime game-event bus ({@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}). */
			FORGE
		}

		Bus bus() default Bus.FORGE;

		Dist[] value() default {Dist.CLIENT, Dist.DEDICATED_SERVER};

		String modid() default "";
	}
}
