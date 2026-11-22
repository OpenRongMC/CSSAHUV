package net.minecraftforge.registries;


import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * Forge registry handles backed by the matching vanilla {@link BuiltInRegistries} registries and
 * {@link Registries} keys.
 */
public final class ForgeRegistries {
	public static final IForgeRegistry<Block> BLOCKS = of(Registries.BLOCK, BuiltInRegistries.BLOCK);
	public static final IForgeRegistry<Item> ITEMS = of(Registries.ITEM, BuiltInRegistries.ITEM);
	public static final IForgeRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = of(Registries.BLOCK_ENTITY_TYPE, BuiltInRegistries.BLOCK_ENTITY_TYPE);
	public static final IForgeRegistry<EntityType<?>> ENTITY_TYPES = of(Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE);
	public static final IForgeRegistry<SoundEvent> SOUND_EVENTS = of(Registries.SOUND_EVENT, BuiltInRegistries.SOUND_EVENT);
	public static final IForgeRegistry<MenuType<?>> MENU_TYPES = of(Registries.MENU, BuiltInRegistries.MENU);

	private ForgeRegistries() {
	}

	private static <T> IForgeRegistry<T> of(ResourceKey<? extends Registry<T>> key, Registry<T> registry) {
		return new IForgeRegistry<T>() {
			@Override
			public ResourceKey<? extends Registry<T>> key() {
				return key;
			}

			@Override
			public Registry<T> registry() {
				return registry;
			}

			@Override
			public T getValue(Identifier id) {
				return registry.getValue(id);
			}
		};
	}
}
