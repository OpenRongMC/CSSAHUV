package net.minecraftforge.fluids.capability.templates;

import net.minecraft.nbt.CompoundTag;

/**
 * Minimal working stand-in for Forge's {@code FluidTank}. Fluids are not modelled on this Fabric
 * shim, so the tank is always empty; only the API surface the ported mod relies on
 * (capacity, NBT round-trip, and the overridable {@link #onContentsChanged()} hook) is provided.
 */
public class FluidTank {
	protected int capacity;

	public FluidTank(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public FluidTank setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	public int getFluidAmount() {
		return 0;
	}

	public boolean isEmpty() {
		return true;
	}

	public FluidTank readFromNBT(CompoundTag nbt) {
		// No fluid state to restore on this shim.
		return this;
	}

	public CompoundTag writeToNBT(CompoundTag nbt) {
		// Nothing to persist on this shim.
		return nbt;
	}

	protected void onContentsChanged() {
	}
}
