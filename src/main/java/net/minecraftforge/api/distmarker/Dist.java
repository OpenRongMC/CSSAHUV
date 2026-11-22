package net.minecraftforge.api.distmarker;

/**
 * Physical side marker, mirroring Forge's {@code net.minecraftforge.api.distmarker.Dist}.
 */
public enum Dist {
	CLIENT,
	DEDICATED_SERVER;

	public boolean isClient() {
		return this == CLIENT;
	}

	public boolean isDedicatedServer() {
		return this == DEDICATED_SERVER;
	}
}
