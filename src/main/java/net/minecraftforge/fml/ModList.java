package net.minecraftforge.fml;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Mirrors Forge's {@code ModList}; backed by the Fabric loader for {@link #isLoaded(String)}.
 */
public final class ModList {
	private static final ModList INSTANCE = new ModList();

	private ModList() {
	}

	public static ModList get() {
		return INSTANCE;
	}

	public boolean isLoaded(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}
}
