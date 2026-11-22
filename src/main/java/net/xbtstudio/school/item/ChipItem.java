
package net.xbtstudio.school.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class ChipItem extends Item {
	public ChipItem() {
		super(new Item.Properties().setId(net.minecraftforge.registries.DeferredRegister.currentItemKey()).stacksTo(64).rarity(Rarity.COMMON));
	}
}
