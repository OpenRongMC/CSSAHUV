#!/usr/bin/env python3
"""Mass-transform CSSAHUV Forge 1.20.1 Mojang names to Minecraft 26.3 + Fabric."""
from __future__ import annotations

import json
import re
import shutil
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java"
RES = ROOT / "src/main/resources"
FORGE_UPSTREAM = ROOT.parent / "CSSAHUV" / "src/main"


def transform_java(text: str, path: Path) -> str:
    # Identifier
    text = text.replace("net.minecraft.resources.ResourceLocation", "net.minecraft.resources.Identifier")
    text = re.sub(r"\bResourceLocation\b", "Identifier", text)
    text = re.sub(
        r"new Identifier\(\s*([^,\n]+)\s*,\s*([^)\n]+)\s*\)",
        r"Identifier.fromNamespaceAndPath(\1, \2)",
        text,
    )
    text = re.sub(r"new Identifier\(\s*([^)\n]+)\s*\)", r"Identifier.parse(\1)", text)

    # DirectionProperty removed
    text = text.replace(
        "net.minecraft.world.level.block.state.properties.DirectionProperty",
        "net.minecraft.world.level.block.state.properties.EnumProperty",
    )
    text = re.sub(r"\bDirectionProperty\b", "EnumProperty<Direction>", text)
    if "EnumProperty<Direction>" in text and "import net.minecraft.core.Direction;" not in text:
        text = text.replace(
            "import net.minecraft.world.level.block.state.properties.EnumProperty;",
            "import net.minecraft.world.level.block.state.properties.EnumProperty;\nimport net.minecraft.core.Direction;",
        )

    # Block property id injection for 26.3
    text = text.replace(
        "super(BlockBehaviour.Properties.of()",
        "super(BlockBehaviour.Properties.of().setId(net.minecraftforge.registries.DeferredRegister.currentBlockKey())",
    )
    text = text.replace(
        "super(new Item.Properties()",
        "super(new Item.Properties().setId(net.minecraftforge.registries.DeferredRegister.currentItemKey())",
    )
    # Door/Slab/Wall may call super with Properties differently
    text = text.replace(
        "super(BlockBehaviour.Properties.of().",
        "super(BlockBehaviour.Properties.of().setId(net.minecraftforge.registries.DeferredRegister.currentBlockKey()).",
    )
    # undo double setId
    text = text.replace(
        ".setId(net.minecraftforge.registries.DeferredRegister.currentBlockKey()).setId(net.minecraftforge.registries.DeferredRegister.currentBlockKey())",
        ".setId(net.minecraftforge.registries.DeferredRegister.currentBlockKey())",
    )

    # noCollission rename (if any)
    text = text.replace(".noCollission()", ".noCollision()")

    # Remove getLightBlock overrides (signature removed / unused)
    text = re.sub(
        r"\s*@Override\s+public int getLightBlock\([^)]*\)\s*\{[^{}]*\}",
        "",
        text,
    )

    # propagatesSkylightDown
    text = re.sub(
        r"propagatesSkylightDown\(\s*BlockState\s+(\w+)\s*,\s*BlockGetter\s+\w+\s*,\s*BlockPos\s+\w+\s*\)",
        r"propagatesSkylightDown(BlockState \1)",
        text,
    )

    # updateShape 1.20.1 -> 26.3
    text = re.sub(
        r"updateShape\(\s*BlockState\s+(\w+)\s*,\s*Direction\s+(\w+)\s*,\s*BlockState\s+(\w+)\s*,\s*LevelAccessor\s+(\w+)\s*,\s*BlockPos\s+(\w+)\s*,\s*BlockPos\s+(\w+)\s*\)",
        r"updateShape(BlockState \1, net.minecraft.world.level.LevelReader \4, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos \5, Direction \2, BlockPos \6, BlockState \3, net.minecraft.util.RandomSource random)",
        text,
    )
    text = re.sub(
        r"(\w+)\.scheduleTick\(",
        lambda m: "ticks.scheduleTick(" if m.group(1) not in {"world", "level"} else "ticks.scheduleTick(",
        text,
    )
    # Fix remaining world.scheduleTick after waterlogged transforms more carefully
    text = text.replace("world.scheduleTick(", "ticks.scheduleTick(")
    text = text.replace(
        "super.updateShape(state, facing, facingState, world, currentPos, facingPos)",
        "super.updateShape(state, world, ticks, currentPos, facing, facingPos, facingState, random)",
    )
    text = text.replace(
        "super.updateShape(blockstate, facing, facingState, world, currentPos, facingPos)",
        "super.updateShape(blockstate, world, ticks, currentPos, facing, facingPos, facingState, random)",
    )

    # mirror rotate helper
    text = text.replace(
        "state.rotate(mirrorIn.getRotation(state.getValue(FACING)))",
        "this.rotate(state, mirrorIn.getRotation(state.getValue(FACING)))",
    )

    # use -> useWithoutItem (drop InteractionHand)
    text = re.sub(
        r"public InteractionResult use\(\s*BlockState\s+(\w+)\s*,\s*Level\s+(\w+)\s*,\s*BlockPos\s+(\w+)\s*,\s*Player\s+(\w+)\s*,\s*InteractionHand\s+(\w+)\s*,\s*BlockHitResult\s+(\w+)\s*\)",
        r"public InteractionResult useWithoutItem(BlockState \1, Level \2, BlockPos \3, Player \4, BlockHitResult \6)",
        text,
    )
    text = text.replace("super.use(", "super.useWithoutItem(")
    # Remove leftover hand arg in super.useWithoutItem(state, world, pos, entity, hand, hit)
    text = re.sub(
        r"super\.useWithoutItem\(\s*(\w+)\s*,\s*(\w+)\s*,\s*(\w+)\s*,\s*(\w+)\s*,\s*\w+\s*,\s*(\w+)\s*\)",
        r"super.useWithoutItem(\1, \2, \3, \4, \5)",
        text,
    )

    # LevelAccessor import may become unused; keep LevelReader usage fully qualified above

    # SoundEvent createVariableRangeEvent already uses Identifier after rename

    # MobType removed
    text = re.sub(
        r"\s*@Override\s+public MobType getMobType\(\)\s*\{[^{}]*\}",
        "",
        text,
    )
    text = text.replace("import net.minecraft.world.entity.MobType;\n", "")

    # setMaxUpStep removed in newer versions — replace with setSpeed / ignore
    text = re.sub(r"\s*setMaxUpStep\([^)]*\);", "", text)

    # getAddEntityPacket() no-arg -> keep but NetworkHooks stub will handle; update signature if override
    text = text.replace(
        "public Packet<ClientGamePacketListener> getAddEntityPacket()",
        "public Packet<ClientGamePacketListener> getAddEntityPacket(net.minecraft.server.level.ServerEntity serverEntity)",
    )
    text = text.replace(
        "return NetworkHooks.getEntitySpawningPacket(this);",
        "return super.getAddEntityPacket(serverEntity);",
    )

    # BlockEntity load/save CompoundTag -> ValueInput/ValueOutput (common MCreator pattern)
    if "extends RandomizableContainerBlockEntity" in text or "extends BlockEntity" in text:
        text = text.replace(
            "import net.minecraft.nbt.CompoundTag;",
            "import net.minecraft.nbt.CompoundTag;\nimport net.minecraft.world.level.storage.ValueInput;\nimport net.minecraft.world.level.storage.ValueOutput;\nimport net.minecraft.core.HolderLookup;",
        )
        text = re.sub(
            r"public void load\(CompoundTag compound\)\s*\{",
            "protected void loadAdditional(ValueInput input) {\n\t\tsuper.loadAdditional(input);\n\t\tCompoundTag compound = new CompoundTag(); // compat bridge\n\t\t// Prefer ValueInput item load below when stacks present",
            text,
            count=1,
        )
        # Simpler dedicated rewrite for printer-like BEs done in post_fix_block_entities

    # appendHoverText on blocks — 26.3 removed Block.appendHoverText(ItemStack, BlockGetter...); drop overrides to compile
    text = re.sub(
        r"\s*@Override\s+public void appendHoverText\(ItemStack[^)]*\)\s*\{(?:[^{}]|\{[^{}]*\})*\}",
        "",
        text,
    )

    # Item appendHoverText signature — leave for compile iteration

    # Remove checkerframework junk imports
    text = re.sub(r"import org\.checkerframework\.[^;]+;\n", "", text)

    # PlayMessages constructor — keep; shim will provide

    return text


def post_fix_block_entities(path: Path, text: str) -> str:
    if "BlockEntity" not in path.name and "/block/entity/" not in str(path).replace("\\", "/"):
        return text
    # Replace load/saveAdditional bodies with ValueInput/Output versions when ContainerHelper present
    if "ContainerHelper" in text:
        text = re.sub(
            r"@Override\s+protected void loadAdditional\(ValueInput input\) \{.*?^\t\}",
            '''@Override
\tprotected void loadAdditional(ValueInput input) {
\t\tsuper.loadAdditional(input);
\t\tthis.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
\t\tContainerHelper.loadAllItems(input, this.stacks);
\t}''',
            text,
            count=1,
            flags=re.S | re.M,
        )
        # If old load(CompoundTag) still present, replace whole method
        text = re.sub(
            r"@Override\s+public void load\(CompoundTag compound\)\s*\{(?:[^{}]|\{[^{}]*\})*\}",
            '''@Override
\tprotected void loadAdditional(ValueInput input) {
\t\tsuper.loadAdditional(input);
\t\tthis.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
\t\tContainerHelper.loadAllItems(input, this.stacks);
\t}''',
            text,
            count=1,
        )
        text = re.sub(
            r"@Override\s+public void saveAdditional\(CompoundTag compound\)\s*\{(?:[^{}]|\{[^{}]*\})*\}",
            '''@Override
\tprotected void saveAdditional(ValueOutput output) {
\t\tsuper.saveAdditional(output);
\t\tContainerHelper.saveAllItems(output, this.stacks);
\t}''',
            text,
            count=1,
        )
        text = re.sub(
            r"public CompoundTag getUpdateTag\(\)\s*\{(?:[^{}]|\{[^{}]*\})*\}",
            '''public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
\t\treturn this.saveWithFullMetadata(registries);
\t}''',
            text,
            count=1,
        )
        if "import net.minecraft.world.level.storage.ValueInput;" not in text:
            text = text.replace(
                "package ",
                "package ",
                1,
            )
            text = text.replace(
                "import net.minecraft.nbt.CompoundTag;",
                "import net.minecraft.nbt.CompoundTag;\nimport net.minecraft.world.level.storage.ValueInput;\nimport net.minecraft.world.level.storage.ValueOutput;\nimport net.minecraft.core.HolderLookup;",
            )
    return text


def fix_resources() -> None:
    # loot_tables -> loot_table, advancements -> advancement
    data = RES / "data/school"
    mapping = {
        "loot_tables": "loot_table",
        "advancements": "advancement",
    }
    for old, new in mapping.items():
        src = data / old
        dst = data / new
        if src.exists():
            if dst.exists():
                shutil.rmtree(dst)
            src.rename(dst)

    # Generate assets/school/items/*.json from models/item for 1.21.4+ item model system
    items_dir = RES / "assets/school/items"
    models_item = RES / "assets/school/models/item"
    items_dir.mkdir(parents=True, exist_ok=True)
    if models_item.exists():
        for model in models_item.glob("*.json"):
            target = items_dir / model.name
            target.write_text(
                json.dumps({"model": {"type": "minecraft:model", "model": f"school:item/{model.stem}"}}, indent=2)
                + "\n"
            )

    # Fix JPEG-as-PNG textures
    textures = RES / "assets/school/textures"
    if textures.exists():
        try:
            from PIL import Image
        except ImportError:
            return
        for path in textures.rglob("*.png"):
            with path.open("rb") as stream:
                if stream.read(2) != bytes.fromhex("ffd8"):
                    continue
            with Image.open(path) as image:
                pixels = image.copy()
            pixels.save(path, format="PNG")


def ensure_client_entrypoint() -> None:
    path = JAVA / "net/xbtstudio/school/SchoolModClient.java"
    if path.exists():
        return
    path.write_text(
        """package net.xbtstudio.school;

import net.fabricmc.api.ClientModInitializer;
import net.xbtstudio.school.init.SchoolModEntityRenderers;
import net.xbtstudio.school.init.SchoolModModels;
import net.xbtstudio.school.init.SchoolModScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class SchoolModClient implements ClientModInitializer {
\t@Override
\tpublic void onInitializeClient() {
\t\tvar bus = FMLJavaModLoadingContext.get().getModEventBus();
\t\tbus.post(new FMLClientSetupEvent());
\t\t// Force-load client subscriber classes
\t\tSchoolModScreens.class.getName();
\t\tSchoolModEntityRenderers.class.getName();
\t\tSchoolModModels.class.getName();
\t}
}
"""
    )


def rewrite_school_mod() -> None:
    path = JAVA / "net/xbtstudio/school/SchoolMod.java"
    path.write_text(
        """package net.xbtstudio.school;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.xbtstudio.school.init.SchoolModBlockEntities;
import net.xbtstudio.school.init.SchoolModBlocks;
import net.xbtstudio.school.init.SchoolModEntities;
import net.xbtstudio.school.init.SchoolModItems;
import net.xbtstudio.school.init.SchoolModMenus;
import net.xbtstudio.school.init.SchoolModSounds;
import net.xbtstudio.school.init.SchoolModTabs;

public class SchoolMod implements ModInitializer {
\tpublic static final Logger LOGGER = LogManager.getLogger(SchoolMod.class);
\tpublic static final String MODID = "school";

\tprivate static final String PROTOCOL_VERSION = "1";
\tpublic static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(
\t\t\tIdentifier.fromNamespaceAndPath(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
\tprivate static int messageID = 0;
\tprivate static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

\t@Override
\tpublic void onInitialize() {
\t\tIEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
\t\tSchoolModSounds.REGISTRY.register(bus);
\t\tSchoolModBlocks.REGISTRY.register(bus);
\t\tSchoolModBlockEntities.REGISTRY.register(bus);
\t\tSchoolModItems.REGISTRY.register(bus);
\t\tSchoolModEntities.REGISTRY.register(bus);
\t\tSchoolModTabs.REGISTRY.register(bus);
\t\tSchoolModMenus.REGISTRY.register(bus);

\t\tnet.minecraftforge.ForgeBootstrap.bootstrap();
\t\tbus.post(new FMLCommonSetupEvent());

\t\tServerTickEvents.END_SERVER_TICK.register(server -> {
\t\t\tMinecraftForge.EVENT_BUS.post(new TickEvent.ServerTickEvent(TickEvent.Phase.END));
\t\t\tList<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
\t\t\tworkQueue.forEach(work -> {
\t\t\t\twork.setValue(work.getValue() - 1);
\t\t\t\tif (work.getValue() == 0)
\t\t\t\t\tactions.add(work);
\t\t\t});
\t\t\tactions.forEach(e -> e.getKey().run());
\t\t\tworkQueue.removeAll(actions);
\t\t});
\t}

\tpublic static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder,
\t\t\tBiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
\t\tPACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
\t\tmessageID++;
\t}

\tpublic static void queueServerWork(int tick, Runnable action) {
\t\tworkQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
\t}
}
"""
    )


def main() -> None:
    # Re-copy forge sources if transform was already applied — expect clean forge tree
    # Caller should reset from upstream before transform when needed.
    count = 0
    for path in sorted(JAVA.rglob("*.java")):
        if "net/minecraftforge/" in str(path).replace("\\", "/"):
            continue
        original = path.read_text(encoding="utf-8")
        text = transform_java(original, path)
        text = post_fix_block_entities(path, text)
        if text != original:
            path.write_text(text, encoding="utf-8")
            count += 1
    rewrite_school_mod()
    ensure_client_entrypoint()
    fix_resources()
    # logo
    for name in ("logo.png", "icon.png"):
        src = FORGE_UPSTREAM / "resources" / "assets" / "school" / name
        if not src.exists():
            src = FORGE_UPSTREAM.parent.parent / "src/main/resources" / name
        # copy icon as logo if needed
    icon = RES / "assets/school/icon.png"
    logo = RES / "logo.png"
    if icon.exists() and not logo.exists():
        shutil.copyfile(icon, logo)
    print(f"Transformed {count} java files")


if __name__ == "__main__":
    main()
