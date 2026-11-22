package net.minecraftforge.eventbus.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event listener. The method must take exactly one argument which is a
 * subclass of {@link Event}. Mirrors Forge's {@code @SubscribeEvent}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubscribeEvent {
}
