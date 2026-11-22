package net.minecraftforge.registries;

import java.util.function.Supplier;

import net.minecraft.resources.Identifier;

/**
 * A handle to a registered object. Mirrors Forge's {@code RegistryObject}: it is a {@link Supplier}
 * exposing {@link #get()} and the registration {@link #getId() id}. On this shim the value is
 * created eagerly by {@link DeferredRegister#register}, so {@link #get()} never returns {@code null}.
 */
public final class RegistryObject<T> implements Supplier<T> {
	private final Identifier id;
	private final T value;

	RegistryObject(Identifier id, T value) {
		this.id = id;
		this.value = value;
	}

	@Override
	public T get() {
		return this.value;
	}

	public Identifier getId() {
		return this.id;
	}

	public boolean isPresent() {
		return this.value != null;
	}
}
