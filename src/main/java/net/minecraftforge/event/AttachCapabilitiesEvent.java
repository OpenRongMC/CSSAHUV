package net.minecraftforge.event;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.eventbus.api.Event;

import net.minecraft.resources.Identifier;

/**
 * Fired on the FORGE bus when capabilities may be attached to an object (entity, block entity,
 * item stack, ...). Mirrors Forge's {@code AttachCapabilitiesEvent<T>}.
 */
public class AttachCapabilitiesEvent<T> extends Event {
	private final T object;
	private final Map<Identifier, ICapabilitySerializable<?>> capabilities = new LinkedHashMap<>();

	public AttachCapabilitiesEvent(T object) {
		this.object = object;
	}

	public T getObject() {
		return this.object;
	}

	public void addCapability(Identifier key, ICapabilitySerializable<?> capability) {
		this.capabilities.put(key, capability);
	}

	public Map<Identifier, ICapabilitySerializable<?>> getCapabilities() {
		return this.capabilities;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}
}
