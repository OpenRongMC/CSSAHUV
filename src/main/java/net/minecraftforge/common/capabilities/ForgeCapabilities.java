package net.minecraftforge.common.capabilities;

import net.minecraftforge.items.IItemHandler;

/**
 * Well-known capability instances. Mirrors Forge's {@code ForgeCapabilities}. Only the handles
 * referenced by the ported mod are provided.
 */
public final class ForgeCapabilities {
	public static final Capability<IItemHandler> ITEM_HANDLER = new Capability<>("item_handler");
	public static final Capability<Object> FLUID_HANDLER = new Capability<>("fluid_handler");

	private ForgeCapabilities() {
	}
}
