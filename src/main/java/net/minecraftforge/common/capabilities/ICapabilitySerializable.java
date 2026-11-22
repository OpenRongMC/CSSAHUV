package net.minecraftforge.common.capabilities;


import net.minecraftforge.common.util.LazyOptional;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;

/**
 * A capability provider that can serialize its attached data. Mirrors the shape of Forge's
 * {@code ICapabilitySerializable<T>} (which combines {@code ICapabilityProvider} and
 * {@code INBTSerializable}).
 */
public interface ICapabilitySerializable<T extends Tag> {
	<X> LazyOptional<X> getCapability(Capability<X> cap, Direction side);

	default <X> LazyOptional<X> getCapability(Capability<X> cap) {
		return getCapability(cap, null);
	}

	T serializeNBT();

	void deserializeNBT(T nbt);
}
