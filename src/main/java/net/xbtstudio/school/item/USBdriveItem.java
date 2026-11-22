
package net.xbtstudio.school.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class USBdriveItem extends Item {
	public USBdriveItem() {
		super(new Item.Properties().setId(net.minecraftforge.registries.DeferredRegister.currentItemKey()).stacksTo(1).rarity(Rarity.COMMON));
	}
}
