package net.minecraftforge.registries;


import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/**
 * Minimal Forge-registry facade over a vanilla {@link Registry}. Carries both the registry
 * {@link #key()} (used by {@link DeferredRegister} to build {@link ResourceKey}s for
 * {@code BlockBehaviour.Properties.setId}/{@code Item.Properties.setId}) and the underlying
 * {@link #registry()} into which values are registered.
 */
public interface IForgeRegistry<T> {
	ResourceKey<? extends Registry<T>> key();

	Registry<T> registry();

	T getValue(Identifier id);
}
