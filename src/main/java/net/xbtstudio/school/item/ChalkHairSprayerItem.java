
package net.xbtstudio.school.item;

import net.xbtstudio.school.entity.ChalkHairSprayerProjectileEntity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;

public class ChalkHairSprayerItem extends Item {
	public ChalkHairSprayerItem() {
		super(new Item.Properties().setId(net.minecraftforge.registries.DeferredRegister.currentItemKey()).durability(6).rarity(Rarity.COMMON));
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack itemstack) {
		return ItemUseAnimation.BOW;
	}

	@Override
	public int getUseDuration(ItemStack itemstack, LivingEntity entity) {
		return 72000;
	}

	@Override
	public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
		return 0f;
	}

	@Override
	public InteractionResult use(Level world, Player entity, InteractionHand hand) {
		if (entity.getAbilities().instabuild || findAmmo(entity) != ItemStack.EMPTY) {
			entity.startUsingItem(hand);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

	@Override
	public boolean releaseUsing(ItemStack itemstack, Level world, LivingEntity entity, int time) {
		if (!world.isClientSide() && entity instanceof ServerPlayer player) {
			ItemStack stack = findAmmo(player);
			if (player.getAbilities().instabuild || stack != ItemStack.EMPTY) {
				ChalkHairSprayerProjectileEntity projectile = ChalkHairSprayerProjectileEntity.shoot(world, entity, world.getRandom());
				itemstack.hurtAndBreak(1, entity, entity.getUsedItemHand());
				if (player.getAbilities().instabuild) {
					projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
				} else {
					if (stack.isDamageableItem()) {
						stack.hurtAndBreak(1, entity, entity.getUsedItemHand());
					if (stack.getDamageValue() >= stack.getMaxDamage()) {
							stack.shrink(1);
							stack.setDamageValue(0);
							if (stack.isEmpty())
								player.getInventory().removeItem(stack);
						}
					} else {
						stack.shrink(1);
						if (stack.isEmpty())
							player.getInventory().removeItem(stack);
					}
				}
			}
		}
		return true;
	}

	private ItemStack findAmmo(Player player) {
		ItemStack stack = ProjectileWeaponItem.getHeldProjectile(player, e -> e.getItem() == ChalkHairSprayerProjectileEntity.PROJECTILE_ITEM.getItem());
		if (stack == ItemStack.EMPTY) {
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack teststack = player.getInventory().getItem(i);
				if (teststack != null && teststack.getItem() == ChalkHairSprayerProjectileEntity.PROJECTILE_ITEM.getItem()) {
					stack = teststack;
					break;
				}
			}
		}
		return stack;
	}
}
