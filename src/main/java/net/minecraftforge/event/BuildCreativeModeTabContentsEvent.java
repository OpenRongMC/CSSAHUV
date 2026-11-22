package net.minecraftforge.event;

import net.minecraftforge.eventbus.api.Event;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * Fired on the MOD bus while a creative tab's contents are being assembled, letting mods add to
 * existing (including vanilla) tabs. Mirrors Forge's {@code BuildCreativeModeTabContentsEvent}.
 */
public class BuildCreativeModeTabContentsEvent extends Event {
	private final ResourceKey<CreativeModeTab> tabKey;
	private final CreativeModeTab tab;
	private final CreativeModeTab.Output output;

	public BuildCreativeModeTabContentsEvent(ResourceKey<CreativeModeTab> tabKey, CreativeModeTab tab, CreativeModeTab.Output output) {
		this.tabKey = tabKey;
		this.tab = tab;
		this.output = output;
	}

	public ResourceKey<CreativeModeTab> getTabKey() {
		return this.tabKey;
	}

	public CreativeModeTab getTab() {
		return this.tab;
	}

	public void accept(ItemLike item) {
		this.output.accept(item);
	}

	public void accept(ItemStack stack) {
		this.output.accept(stack);
	}

	@Override
	public boolean isCancelable() {
		return false;
	}
}
