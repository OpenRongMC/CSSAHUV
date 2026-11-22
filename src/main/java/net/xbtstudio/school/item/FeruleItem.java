package net.xbtstudio.school.item;

import net.xbtstudio.school.procedures.FERULTProcedure;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.InteractionResult;

public class FeruleItem extends Item {
	public FeruleItem() {
		super(new Item.Properties().setId(net.minecraftforge.registries.DeferredRegister.currentItemKey()).stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		FERULTProcedure.execute(context.getLevel(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ());
		return InteractionResult.SUCCESS;
	}
}
