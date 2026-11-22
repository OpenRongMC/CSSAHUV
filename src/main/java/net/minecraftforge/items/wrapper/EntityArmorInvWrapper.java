package net.minecraftforge.items.wrapper;

import net.minecraftforge.items.IItemHandler;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Exposes a {@link LivingEntity}'s armor slots as an {@link IItemHandler}. Mirrors Forge's
 * {@code EntityArmorInvWrapper}.
 */
public class EntityArmorInvWrapper implements IItemHandler {
	private static final EquipmentSlot[] SLOTS = {EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};

	protected final LivingEntity entity;

	public EntityArmorInvWrapper(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public int getSlots() {
		return SLOTS.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.entity.getItemBySlot(SLOTS[slot]);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		ItemStack existing = this.entity.getItemBySlot(SLOTS[slot]);
		if (!existing.isEmpty())
			return stack;
		if (!simulate)
			this.entity.setItemSlot(SLOTS[slot], stack.copy());
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount <= 0)
			return ItemStack.EMPTY;
		ItemStack existing = this.entity.getItemBySlot(SLOTS[slot]);
		if (existing.isEmpty())
			return ItemStack.EMPTY;
		ItemStack extracted = existing.copy();
		extracted.setCount(Math.min(amount, existing.getCount()));
		if (!simulate) {
			ItemStack remainder = existing.copy();
			remainder.shrink(extracted.getCount());
			this.entity.setItemSlot(SLOTS[slot], remainder.isEmpty() ? ItemStack.EMPTY : remainder);
		}
		return extracted;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return true;
	}
}
