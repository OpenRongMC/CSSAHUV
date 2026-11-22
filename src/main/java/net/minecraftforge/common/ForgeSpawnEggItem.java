package net.minecraftforge.common;

import java.util.function.Supplier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

/**
 * Spawn-egg item for a modded entity type. Forge's version subclasses {@code SpawnEggItem}; because
 * {@code SpawnEggItem}'s constructor shape is version-sensitive, this shim extends {@link Item} and
 * simply carries the entity-type supplier and egg colors (a working, compilable stub). Entity
 * placement behavior can be layered on later if required.
 */
public class ForgeSpawnEggItem extends Item {
	private final Supplier<? extends EntityType<?>> typeSupplier;
	private final int backgroundColor;
	private final int highlightColor;

	public ForgeSpawnEggItem(Supplier<? extends EntityType<?>> type, int backgroundColor, int highlightColor, Item.Properties properties) {
		super(properties);
		this.typeSupplier = type;
		this.backgroundColor = backgroundColor;
		this.highlightColor = highlightColor;
	}

	public EntityType<?> getType() {
		return this.typeSupplier.get();
	}

	public int getColor(int tintIndex) {
		return tintIndex == 0 ? this.backgroundColor : this.highlightColor;
	}
}
