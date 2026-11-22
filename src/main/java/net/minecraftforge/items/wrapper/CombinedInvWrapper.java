package net.minecraftforge.items.wrapper;

import net.minecraftforge.items.IItemHandler;

import net.minecraft.world.item.ItemStack;

/**
 * Presents several {@link IItemHandler}s as one, with slot indices laid out end-to-end. Mirrors
 * Forge's {@code CombinedInvWrapper}.
 */
public class CombinedInvWrapper implements IItemHandler {
	protected final IItemHandler[] handlers;
	protected final int[] baseIndex;
	protected final int slotCount;

	public CombinedInvWrapper(IItemHandler... handlers) {
		this.handlers = handlers;
		this.baseIndex = new int[handlers.length];
		int index = 0;
		for (int i = 0; i < handlers.length; i++) {
			index += handlers[i].getSlots();
			this.baseIndex[i] = index;
		}
		this.slotCount = index;
	}

	private int getHandlerIndexForSlot(int slot) {
		for (int i = 0; i < this.baseIndex.length; i++) {
			if (slot < this.baseIndex[i])
				return i;
		}
		return -1;
	}

	private int getLocalSlot(int handler, int slot) {
		return handler == 0 ? slot : slot - this.baseIndex[handler - 1];
	}

	@Override
	public int getSlots() {
		return this.slotCount;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		int h = getHandlerIndexForSlot(slot);
		return this.handlers[h].getStackInSlot(getLocalSlot(h, slot));
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		int h = getHandlerIndexForSlot(slot);
		return this.handlers[h].insertItem(getLocalSlot(h, slot), stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		int h = getHandlerIndexForSlot(slot);
		return this.handlers[h].extractItem(getLocalSlot(h, slot), amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		int h = getHandlerIndexForSlot(slot);
		return this.handlers[h].getSlotLimit(getLocalSlot(h, slot));
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		int h = getHandlerIndexForSlot(slot);
		return this.handlers[h].isItemValid(getLocalSlot(h, slot), stack);
	}
}
