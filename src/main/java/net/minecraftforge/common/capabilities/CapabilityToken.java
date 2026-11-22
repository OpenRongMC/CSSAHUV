package net.minecraftforge.common.capabilities;

/**
 * Super-type token used to capture a capability's generic type at a call site, e.g.
 * {@code new CapabilityToken<PlayerVariables>() {}}. Mirrors Forge's {@code CapabilityToken<T>}.
 */
public abstract class CapabilityToken<T> {
	public CapabilityToken() {
	}
}
