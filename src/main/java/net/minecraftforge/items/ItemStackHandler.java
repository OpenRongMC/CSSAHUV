package net.minecraftforge.items;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

/**
 * A simple {@link NonNullList}-backed {@link IItemHandler}, mirroring Forge's
 * {@code ItemStackHandler} (including NBT (de)serialization and the overridable
 * {@link #onContentsChanged(int)} hook).
 */
public class ItemStackHandler implements IItemHandler {
	protected NonNullList<ItemStack> stacks;

	public ItemStackHandler() {
		this(1);
	}

	public ItemStackHandler(int size) {
		this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	public ItemStackHandler(NonNullList<ItemStack> stacks) {
		this.stacks = stacks;
	}

	public void setSize(int size) {
		this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	public void setStackInSlot(int slot, ItemStack stack) {
		validateSlotIndex(slot);
		this.stacks.set(slot, stack);
		onContentsChanged(slot);
	}

	@Override
	public int getSlots() {
		return this.stacks.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		validateSlotIndex(slot);
		return this.stacks.get(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		if (!isItemValid(slot, stack))
			return stack;
		validateSlotIndex(slot);
		ItemStack existing = this.stacks.get(slot);
		int limit = getStackLimit(slot, stack);
		if (!existing.isEmpty()) {
			if (!ItemStack.isSameItemSameComponents(stack, existing))
				return stack;
			limit -= existing.getCount();
		}
		if (limit <= 0)
			return stack;
		boolean reachedLimit = stack.getCount() > limit;
		if (!simulate) {
			if (existing.isEmpty()) {
				this.stacks.set(slot, reachedLimit ? copyWithSize(stack, limit) : stack);
			} else {
				existing.grow(reachedLimit ? limit : stack.getCount());
			}
			onContentsChanged(slot);
		}
		return reachedLimit ? copyWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount == 0)
			return ItemStack.EMPTY;
		validateSlotIndex(slot);
		ItemStack existing = this.stacks.get(slot);
		if (existing.isEmpty())
			return ItemStack.EMPTY;
		int toExtract = Math.min(amount, existing.getMaxStackSize());
		if (existing.getCount() <= toExtract) {
			if (!simulate) {
				this.stacks.set(slot, ItemStack.EMPTY);
				onContentsChanged(slot);
				return existing;
			}
			return existing.copy();
		} else {
			if (!simulate) {
				this.stacks.set(slot, copyWithSize(existing, existing.getCount() - toExtract));
				onContentsChanged(slot);
			}
			return copyWithSize(existing, toExtract);
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	protected int getStackLimit(int slot, ItemStack stack) {
		return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return true;
	}

	protected void onContentsChanged(int slot) {
	}

	protected void onLoad() {
	}

	public CompoundTag serializeNBT() {
		ListTag items = new ListTag();
		for (int i = 0; i < this.stacks.size(); i++) {
			ItemStack stack = this.stacks.get(i);
			if (!stack.isEmpty()) {
				CompoundTag itemTag = new CompoundTag();
				itemTag.putInt("Slot", i);
				ItemStack.CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, stack).result().ifPresent(tag -> {
					if (tag instanceof CompoundTag compound) {
						itemTag.merge(compound);
					}
				});
				items.add(itemTag);
			}
		}
		CompoundTag nbt = new CompoundTag();
		nbt.put("Items", items);
		nbt.putInt("Size", this.stacks.size());
		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		setSize(nbt.contains("Size") ? nbt.getIntOr("Size", this.stacks.size()) : this.stacks.size());
		ListTag items = nbt.getListOrEmpty("Items");
		for (int i = 0; i < items.size(); i++) {
			CompoundTag itemTag = items.getCompoundOrEmpty(i);
			int slot = itemTag.getIntOr("Slot", -1);
			if (slot >= 0 && slot < this.stacks.size()) {
				ItemStack.CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, itemTag).result().ifPresent(stack -> this.stacks.set(slot, stack));
			}
		}
		onLoad();
	}

	private static ItemStack copyWithSize(ItemStack stack, int size) {
		if (size <= 0)
			return ItemStack.EMPTY;
		ItemStack copy = stack.copy();
		copy.setCount(size);
		return copy;
	}

	private void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= this.stacks.size())
			throw new IllegalArgumentException("Slot " + slot + " not in valid range - [0," + this.stacks.size() + ")");
	}
}
