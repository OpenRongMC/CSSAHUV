package net.minecraftforge.registries;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

import net.minecraftforge.eventbus.api.IEventBus;

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
 * Fabric-side re-implementation of Forge's {@code DeferredRegister}.
 *
 * <p>Key behavioural contract (relied upon by the ported MCreator code): {@link #register(String,
 * Supplier)} publishes the registration {@link Identifier} onto a per-thread stack and then
 * <em>immediately</em> invokes the supplier. Nested {@code register(...)} calls (e.g. a spawn-egg
 * supplier touching an entity DeferredRegister) push/pop correctly so the outer id remains
 * visible. Constructors can call
 * {@code BlockBehaviour.Properties.of().setId(DeferredRegister.currentBlockKey())}. The created
 * object is cached in the returned {@link RegistryObject}; the actual insertion into the vanilla
 * {@link BuiltInRegistries} happens later from {@link #register(IEventBus)} /
 * {@link #registerNow()}.</p>
 *
 * @param <T> the registry element type
 */
public final class DeferredRegister<T> {
	/** Nested pending ids for the registration whose supplier is currently executing. */
	private static final ThreadLocal<Deque<Identifier>> CURRENT_ID_STACK = ThreadLocal.withInitial(ArrayDeque::new);

	/** Peek of the current registration id (null if none). Kept for source compatibility. */
	public static final ThreadLocal<Identifier> CURRENT_ID = new ThreadLocal<>() {
		@Override
		public Identifier get() {
			Deque<Identifier> stack = CURRENT_ID_STACK.get();
			return stack.isEmpty() ? null : stack.peek();
		}

		@Override
		public void set(Identifier value) {
			CURRENT_ID_STACK.get().push(value);
		}

		@Override
		public void remove() {
			Deque<Identifier> stack = CURRENT_ID_STACK.get();
			if (!stack.isEmpty())
				stack.pop();
			if (stack.isEmpty())
				CURRENT_ID_STACK.remove();
		}
	};
	/** Every {@link DeferredRegister} ever created, in creation order. */
	public static final List<DeferredRegister<?>> ALL = new ArrayList<>();

	private final String modid;
	private final ResourceKey<? extends Registry<T>> registryKey;
	private Registry<T> registry;
	private final List<RegistryObject<? extends T>> entries = new ArrayList<>();
	private boolean flushed = false;

	private DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, Registry<T> registry, String modid) {
		this.registryKey = registryKey;
		this.registry = registry;
		this.modid = modid;
		ALL.add(this);
	}

	public static <T> DeferredRegister<T> create(IForgeRegistry<T> registry, String modid) {
		return new DeferredRegister<>(registry.key(), registry.registry(), modid);
	}

	public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registryKey, String modid) {
		return new DeferredRegister<>(registryKey, null, modid);
	}

	// --- Pending-id accessors used by transformed constructors ---------------------------------

	public static ResourceKey<Block> currentBlockKey() {
		return ResourceKey.create(Registries.BLOCK, requireCurrentId());
	}

	public static ResourceKey<Item> currentItemKey() {
		return ResourceKey.create(Registries.ITEM, requireCurrentId());
	}

	public static ResourceKey<EntityType<?>> currentEntityKey() {
		return ResourceKey.create(Registries.ENTITY_TYPE, requireCurrentId());
	}

	public static ResourceKey<SoundEvent> currentSoundKey() {
		return ResourceKey.create(Registries.SOUND_EVENT, requireCurrentId());
	}

	public static ResourceKey<MenuType<?>> currentMenuKey() {
		return ResourceKey.create(Registries.MENU, requireCurrentId());
	}

	public static ResourceKey<BlockEntityType<?>> currentBlockEntityKey() {
		return ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, requireCurrentId());
	}

	private static Identifier requireCurrentId() {
		Identifier id = CURRENT_ID.get();
		if (id == null)
			throw new IllegalStateException("DeferredRegister.currentXKey() called outside of a register(...) supplier");
		return id;
	}

	// --- Registration ---------------------------------------------------------------------------

	public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
		Identifier id = Identifier.fromNamespaceAndPath(this.modid, name);
		CURRENT_ID.set(id);
		I value;
		try {
			value = supplier.get();
		} finally {
			CURRENT_ID.remove();
		}
		RegistryObject<I> object = new RegistryObject<>(id, value);
		this.entries.add(object);
		return object;
	}

	/**
	 * Forge-compatible entry point: the {@link IEventBus} argument is accepted for source
	 * compatibility but is not needed &mdash; registration into the vanilla registry happens here.
	 */
	public void register(IEventBus bus) {
		registerNow();
	}

	/** Inserts every pending entry into the backing vanilla registry. Idempotent. */
	@SuppressWarnings("unchecked")
	public void registerNow() {
		if (this.flushed)
			return;
		Registry<T> target = resolveRegistry();
		for (RegistryObject<? extends T> entry : this.entries) {
			Registry.register((Registry<T>) target, entry.getId(), (T) entry.get());
		}
		this.flushed = true;
	}

	public List<RegistryObject<? extends T>> getEntries() {
		return this.entries;
	}

	@SuppressWarnings("unchecked")
	private Registry<T> resolveRegistry() {
		if (this.registry == null) {
			// MC 26.x: Registry.get(id) returns Optional<Holder.Reference<?>>; use getValue.
			this.registry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(this.registryKey.identifier());
			if (this.registry == null)
				throw new IllegalStateException("No vanilla registry for " + this.registryKey.identifier());
		}
		return this.registry;
	}
}
