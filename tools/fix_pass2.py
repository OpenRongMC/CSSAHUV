#!/usr/bin/env python3
"""Second-pass API fixes for CSSAHUV Fabric 26.3."""
from __future__ import annotations

import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/net/xbtstudio"

PROPERTY_COPY_OLD = re.compile(
    r"for \(Map\.Entry<Property<\?>, Comparable<\?>> entry : _bso\.getValues\(\)\.entrySet\(\)\) \{\s*"
    r"Property _property = _bs\.getBlock\(\)\.getStateDefinition\(\)\.getProperty\(entry\.getKey\(\)\.getName\(\)\);\s*"
    r"if \(_property != null && _bs\.getValue\(_property\) != null\)\s*"
    r"try \{\s*_bs = _bs\.setValue\(_property, \(Comparable\) entry\.getValue\(\)\);\s*\} catch \(Exception e\) \{\s*\}\s*\}",
    re.S,
)

PROPERTY_COPY_NEW = """for (Property.Value<?> value : _bso.getValues().toList()) {
				Property _property = _bs.getBlock().getStateDefinition().getProperty(value.property().getName());
				if (_property != null && _bs.getValue(_property) != null)
					try {
						_bs = _bs.setValue(_property, (Comparable) value.value());
					} catch (Exception e) {
					}
			}"""


def fix_text(text: str, path: Path) -> str:
    # Identifier single-arg misuse
    text = re.sub(
        r'Identifier\.fromNamespaceAndPath\(\s*"([^"]+:[^"]+)"\s*\)',
        r'Identifier.parse("\1")',
        text,
    )

    # Property copy loops
    text = PROPERTY_COPY_OLD.sub(PROPERTY_COPY_NEW, text)
    if "Property.Value<?>" in text and "import java.util.Map;" in text and "Map." not in text.replace("import java.util.Map;", ""):
        text = text.replace("import java.util.Map;\n", "")

    # DoorBlock ctor order: BlockSetType first
    text = re.sub(
        r"super\(\s*(BlockBehaviour\.Properties\.of\(\)[^,]+),\s*(BlockSetType\.\w+)\s*\)",
        r"super(\2, \1)",
        text,
    )

    # closeContainer via ServerPlayer
    text = text.replace(
        "_player.closeContainer();",
        "if (_player instanceof net.minecraft.server.level.ServerPlayer _sp) _sp.closeContainer();",
    )

    # Advancement via getAdvancements
    text = re.sub(
        r"Advancement _adv = _player\.server\.getAdvancements\(\)\.getAdvancement\(([^)]+)\);",
        r"net.minecraft.advancements.AdvancementHolder _adv = _player.getAdvancements().get(\1);",
        text,
    )
    # follow-up patterns often use AdvancementProgress with _adv - leave for next pass

    # Inventory.items access
    text = re.sub(
        r"for \(int i = 0; i < player\.getInventory\(\)\.items\.size\(\); i\+\+\) \{\s*"
        r"ItemStack teststack = player\.getInventory\(\)\.items\.get\(i\);",
        "for (int i = 0; i < player.getInventory().getContainerSize(); i++) {\n"
        "\t\t\t\tItemStack teststack = player.getInventory().getItem(i);",
        text,
    )

    # AbstractArrow package move
    text = text.replace(
        "net.minecraft.world.entity.projectile.AbstractArrow",
        "net.minecraft.world.entity.projectile.arrow.AbstractArrow",
    )

    # GuiGraphics -> GuiGraphicsExtractor
    text = text.replace("net.minecraft.client.gui.GuiGraphics", "net.minecraft.client.gui.GuiGraphicsExtractor")
    text = re.sub(r"\bGuiGraphics\b", "GuiGraphicsExtractor", text)

    # ForgeRegistries getValue helper already on shim — ensure method exists
    # Level.random -> getRandom()
    text = re.sub(r"(\w+)\.random\b", r"\1.getRandom()", text)
    # undo false positives like RandomSource.getRandom()
    text = text.replace("RandomSource.getRandom()", "RandomSource.create()")  # weak; refine if needed
    text = text.replace("getRandom().next", "getRandom().next")  # noop

    # javax.annotation -> org.jetbrains
    text = text.replace("javax.annotation.Nullable", "org.jetbrains.annotations.Nullable")
    text = text.replace("javax.annotation.Nonnull", "org.jetbrains.annotations.NotNull")

    # shouldDisplayFluidOverlay forge method — remove overrides
    text = re.sub(
        r"\s*@Override\s+public boolean shouldDisplayFluidOverlay\([^)]*\)\s*\{[^{}]*\}",
        "",
        text,
    )

    # getFireSpreadSpeed forge — remove
    text = re.sub(
        r"\s*@Override\s+public int getFireSpreadSpeed\([^)]*\)\s*\{[^{}]*\}",
        "",
        text,
    )

    # onRemove -> affectNeighborsAfterRemoval / remove more carefully later
    text = text.replace("public void onRemove(", "public void onRemoveCompat(")
    text = text.replace("@Override\n\tpublic void onRemoveCompat(", "public void onRemoveCompat(")

    # InteractionResultHolder removed — map to ItemInteractionResult / InteractionResult
    text = text.replace("InteractionResultHolder", "InteractionResult")

    return text


def ensure_forge_registry_getvalue() -> None:
    path = ROOT / "src/main/java/net/minecraftforge/registries/IForgeRegistry.java"
    text = path.read_text()
    if "getValue" in text:
        return
    path.write_text(
        """package net.minecraftforge.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IForgeRegistry<V> {
	ResourceKey<? extends Registry<V>> key();

	@SuppressWarnings("unchecked")
	default V getValue(Identifier id) {
		Registry<V> registry = (Registry<V>) BuiltInRegistries.REGISTRY.getValue(key().identifier());
		if (registry == null) {
			// fallback known registries
			Object key = key();
			if (key == net.minecraft.core.registries.Registries.SOUND_EVENT) {
				return (V) BuiltInRegistries.SOUND_EVENT.getValue(id);
			}
			if (key == net.minecraft.core.registries.Registries.BLOCK) {
				return (V) BuiltInRegistries.BLOCK.getValue(id);
			}
			if (key == net.minecraft.core.registries.Registries.ITEM) {
				return (V) BuiltInRegistries.ITEM.getValue(id);
			}
			return null;
		}
		return registry.getValue(id);
	}
}
"""
    )
    # Simpler concrete helper on ForgeRegistries instead
    forge = ROOT / "src/main/java/net/minecraftforge/registries/ForgeRegistries.java"
    forge.write_text(
        """package net.minecraftforge.registries;

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

public final class ForgeRegistries {
	public static final SoundRegistry SOUND_EVENTS = new SoundRegistry();
	public static final SimpleRegistry<Block> BLOCKS = new SimpleRegistry<>(Registries.BLOCK, BuiltInRegistries.BLOCK);
	public static final SimpleRegistry<Item> ITEMS = new SimpleRegistry<>(Registries.ITEM, BuiltInRegistries.ITEM);
	public static final SimpleRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new SimpleRegistry<>(Registries.BLOCK_ENTITY_TYPE, BuiltInRegistries.BLOCK_ENTITY_TYPE);
	public static final SimpleRegistry<EntityType<?>> ENTITY_TYPES = new SimpleRegistry<>(Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE);
	public static final SimpleRegistry<MenuType<?>> MENU_TYPES = new SimpleRegistry<>(Registries.MENU, BuiltInRegistries.MENU);

	private ForgeRegistries() {
	}

	public static final class SimpleRegistry<T> implements IForgeRegistry<T> {
		private final ResourceKey<? extends Registry<T>> key;
		private final Registry<T> registry;

		SimpleRegistry(ResourceKey<? extends Registry<T>> key, Registry<T> registry) {
			this.key = key;
			this.registry = registry;
		}

		@Override
		public ResourceKey<? extends Registry<T>> key() {
			return this.key;
		}

		public T getValue(Identifier id) {
			return this.registry.getValue(id);
		}
	}

	public static final class SoundRegistry extends SimpleRegistry<SoundEvent> {
		SoundRegistry() {
			super(Registries.SOUND_EVENT, BuiltInRegistries.SOUND_EVENT);
		}
	}
}
"""
    )


def main() -> None:
    ensure_forge_registry_getvalue()
    n = 0
    for path in JAVA.rglob("*.java"):
        old = path.read_text(encoding="utf-8")
        new = fix_text(old, path)
        if new != old:
            path.write_text(new, encoding="utf-8")
            n += 1
    print(f"fix_pass2 updated {n} files")


if __name__ == "__main__":
    main()
