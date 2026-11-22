package net.minecraftforge.api.distmarker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compatibility stand-in for Forge's {@code @OnlyIn}. On Fabric this is purely informational;
 * it is not processed by any class transformer, so the annotated members are always present.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface OnlyIn {
	Dist value();

	/**
	 * When non-{@link Object}, marks an interface that is only implemented on the given {@link Dist}.
	 */
	Class<?> _interface() default Object.class;
}
