package net.minecraftforge.items;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * A {@link Slot} that reads/writes an {@link IItemHandler} instead of a vanilla {@link Container}.
 * Mirrors Forge's {@code SlotItemHandler}.
 */
public class SlotItemHandler extends Slot {
	private static final Container EMPTY_INVENTORY = new SimpleContainer(0);

	protected final IItemHandler itemHandler;
	private final int index;

	public SlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(EMPTY_INVENTORY, index, xPosition, yPosition);
		this.itemHandler = itemHandler;
		this.index = index;
	}

	public IItemHandler getItemHandler() {
		return this.itemHandler;
	}

	public int getSlotIndex() {
		return this.index;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		if (stack.isEmpty())
			return false;
		return this.itemHandler.isItemValid(this.index, stack);
	}

	@Override
	public ItemStack getItem() {
		return this.itemHandler.getStackInSlot(this.index);
	}

	@Override
	public void set(ItemStack stack) {
		if (this.itemHandler instanceof ItemStackHandler modifiable)
			modifiable.setStackInSlot(this.index, stack);
		this.setChanged();
	}

	@Override
	public void setChanged() {
	}

	@Override
	public int getMaxStackSize() {
		return this.itemHandler.getSlotLimit(this.index);
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		ItemStack maxAdd = stack.copy();
		int maxInput = stack.getMaxStackSize();
		maxAdd.setCount(maxInput);
		ItemStack remainder = this.itemHandler.extractItem(this.index, maxInput, true);
		int current = remainder.getCount();
		return maxInput - current <= 0 ? this.itemHandler.getSlotLimit(this.index) : Math.min(this.itemHandler.getSlotLimit(this.index), stack.getMaxStackSize());
	}

	@Override
	public boolean mayPickup(Player playerIn) {
		return !this.itemHandler.extractItem(this.index, 1, true).isEmpty();
	}

	@Override
	public ItemStack remove(int amount) {
		return this.itemHandler.extractItem(this.index, amount, false);
	}
}
