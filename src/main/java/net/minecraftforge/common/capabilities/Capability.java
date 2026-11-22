package net.minecraftforge.common.capabilities;

/**
 * Identity token for a capability type. Mirrors Forge's {@code Capability<T>}; instances are
 * compared by reference ({@code cap == ForgeCapabilities.ITEM_HANDLER}).
 */
public final class Capability<T> {
	private final String name;

	Capability(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "Capability[" + this.name + "]";
	}
}
