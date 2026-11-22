package net.minecraftforge.items.wrapper;


import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;

/**
 * Exposes a {@link WorldlyContainer} as an {@link IItemHandler} for one {@link Direction}. Mirrors
 * Forge's {@code SidedInvWrapper}.
 */
public class SidedInvWrapper implements IItemHandler {
	protected final WorldlyContainer inv;
	protected final Direction side;

	public SidedInvWrapper(WorldlyContainer inv, Direction side) {
		this.inv = inv;
		this.side = side;
	}

	/**
	 * Builds one {@link LazyOptional} wrapper per side, matching Forge's helper used by generated
	 * block entities: {@code SidedInvWrapper.create(this, Direction.values())}.
	 */
	@SuppressWarnings("unchecked")
	public static LazyOptional<IItemHandler>[] create(WorldlyContainer inv, Direction... sides) {
		LazyOptional<IItemHandler>[] result = new LazyOptional[sides.length];
		for (int i = 0; i < sides.length; i++) {
			final SidedInvWrapper wrapper = new SidedInvWrapper(inv, sides[i]);
			result[i] = LazyOptional.of(() -> wrapper);
		}
		return result;
	}

	private int[] slotsForFace() {
		return this.inv.getSlotsForFace(this.side);
	}

	@Override
	public int getSlots() {
		return slotsForFace().length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		int target = slotsForFace()[slot];
		return this.inv.getItem(target);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		int target = slotsForFace()[slot];
		if (!this.inv.canPlaceItemThroughFace(target, stack, this.side) || !this.inv.canPlaceItem(target, stack))
			return stack;
		ItemStack existing = this.inv.getItem(target);
		int limit = Math.min(this.inv.getMaxStackSize(), stack.getMaxStackSize());
		if (!existing.isEmpty()) {
			if (!ItemStack.isSameItemSameComponents(stack, existing))
				return stack;
			limit -= existing.getCount();
		}
		if (limit <= 0)
			return stack;
		int accepted = Math.min(limit, stack.getCount());
		if (!simulate) {
			if (existing.isEmpty()) {
				ItemStack copy = stack.copy();
				copy.setCount(accepted);
				this.inv.setItem(target, copy);
			} else {
				existing.grow(accepted);
			}
			this.inv.setChanged();
		}
		ItemStack remainder = stack.copy();
		remainder.setCount(stack.getCount() - accepted);
		return remainder.isEmpty() ? ItemStack.EMPTY : remainder;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount <= 0)
			return ItemStack.EMPTY;
		int target = slotsForFace()[slot];
		ItemStack existing = this.inv.getItem(target);
		if (existing.isEmpty() || !this.inv.canTakeItemThroughFace(target, existing, this.side))
			return ItemStack.EMPTY;
		if (simulate) {
			ItemStack copy = existing.copy();
			copy.setCount(Math.min(amount, existing.getCount()));
			return copy;
		}
		ItemStack extracted = this.inv.removeItem(target, Math.min(amount, existing.getCount()));
		this.inv.setChanged();
		return extracted;
	}

	@Override
	public int getSlotLimit(int slot) {
		return this.inv.getMaxStackSize();
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		int target = slotsForFace()[slot];
		return this.inv.canPlaceItem(target, stack);
	}
}
