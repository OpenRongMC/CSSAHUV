package net.minecraftforge.items;

import net.minecraft.world.item.ItemStack;

/**
 * Forge's item-handler capability interface. Mirrors
 * {@code net.minecraftforge.items.IItemHandler}.
 */
public interface IItemHandler {
	int getSlots();

	ItemStack getStackInSlot(int slot);

	ItemStack insertItem(int slot, ItemStack stack, boolean simulate);

	ItemStack extractItem(int slot, int amount, boolean simulate);

	int getSlotLimit(int slot);

	boolean isItemValid(int slot, ItemStack stack);
}
