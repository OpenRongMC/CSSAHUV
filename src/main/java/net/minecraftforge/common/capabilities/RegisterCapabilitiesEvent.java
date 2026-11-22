package net.minecraftforge.common.capabilities;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.eventbus.api.Event;

/**
 * Fired on the MOD bus so mods can declare their capability types. Mirrors Forge's
 * {@code RegisterCapabilitiesEvent}. On this shim registration is a no-op bookkeeping step
 * (capabilities here are plain reference tokens), but the registered classes are remembered.
 */
public class RegisterCapabilitiesEvent extends Event {
	private final List<Class<?>> registered = new ArrayList<>();

	public void register(Class<?> type) {
		this.registered.add(type);
	}

	public List<Class<?>> getRegistered() {
		return this.registered;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}
}
