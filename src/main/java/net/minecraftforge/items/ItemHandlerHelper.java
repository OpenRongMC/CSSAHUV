package net.minecraftforge.items;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Helper utilities for item handlers, mirroring the subset of Forge's {@code ItemHandlerHelper}
 * used by the ported mod.
 */
public final class ItemHandlerHelper {
	private ItemHandlerHelper() {
	}

	/**
	 * Inserts the stack into the player's inventory, dropping any remainder into the world.
	 */
	public static void giveItemToPlayer(Player player, ItemStack stack) {
		giveItemToPlayer(player, stack, -1);
	}

	public static void giveItemToPlayer(Player player, ItemStack stack, int preferredSlot) {
		if (stack.isEmpty())
			return;
		boolean inserted;
		if (preferredSlot >= 0 && preferredSlot < player.getInventory().getContainerSize()) {
			inserted = player.getInventory().add(preferredSlot, stack);
		} else {
			inserted = player.getInventory().add(stack);
		}
		if (!inserted || !stack.isEmpty()) {
			ItemEntity entity = player.drop(stack, false, net.minecraft.util.Prediction.SERVER_ONLY);
			if (entity != null)
				entity.setNoPickUpDelay();
		} else {
			player.containerMenu.broadcastChanges();
		}
	}

	public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
		if (size == 0 || itemStack.isEmpty())
			return ItemStack.EMPTY;
		ItemStack copy = itemStack.copy();
		copy.setCount(size);
		return copy;
	}

	public static ItemStack insertItem(IItemHandler dest, ItemStack stack, boolean simulate) {
		if (dest == null || stack.isEmpty())
			return stack;
		for (int slot = 0; slot < dest.getSlots(); slot++) {
			stack = dest.insertItem(slot, stack, simulate);
			if (stack.isEmpty())
				return ItemStack.EMPTY;
		}
		return stack;
	}
}
