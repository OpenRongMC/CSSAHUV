package net.minecraftforge.event.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraftforge.eventbus.api.Event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

/**
 * Fired on the MOD bus so mods can supply default attributes for their living entity types.
 * Mirrors Forge's {@code EntityAttributeCreationEvent}.
 *
 * <p>The supplied attributes are recorded in {@link #getCreatedAttributes()} so the Fabric
 * entrypoint can forward them to {@code FabricDefaultAttributeRegistry} (which takes an
 * {@code AttributeSupplier.Builder}); the shim itself does not auto-register them.</p>
 */
public class EntityAttributeCreationEvent extends Event {
	private final Map<EntityType<? extends LivingEntity>, AttributeSupplier> created = new ConcurrentHashMap<>();

	public void put(EntityType<? extends LivingEntity> type, AttributeSupplier attributes) {
		this.created.put(type, attributes);
	}

	public Map<EntityType<? extends LivingEntity>, AttributeSupplier> getCreatedAttributes() {
		return this.created;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}
}
