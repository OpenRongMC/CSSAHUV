package net.minecraftforge.common.capabilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hands out {@link Capability} instances keyed by {@link CapabilityToken}. Mirrors Forge's
 * {@code CapabilityManager.get(CapabilityToken)}. Each distinct token type yields a single, stable
 * capability instance (so reference comparisons work across the mod).
 */
public final class CapabilityManager {
	private static final Map<String, Capability<?>> REGISTRY = new ConcurrentHashMap<>();

	private CapabilityManager() {
	}

	@SuppressWarnings("unchecked")
	public static <T> Capability<T> get(CapabilityToken<T> token) {
		// The token is an anonymous subclass; its class name uniquely identifies the capability.
		String key = token.getClass().getName();
		return (Capability<T>) REGISTRY.computeIfAbsent(key, Capability::new);
	}
}
