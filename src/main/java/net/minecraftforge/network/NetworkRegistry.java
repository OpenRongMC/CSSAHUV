package net.minecraftforge.network;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraftforge.network.simple.SimpleChannel;

import net.minecraft.resources.Identifier;

/**
 * Factory for {@link SimpleChannel}s, mirroring Forge's {@code NetworkRegistry}.
 */
public final class NetworkRegistry {
	private NetworkRegistry() {
	}

	public static SimpleChannel newSimpleChannel(Identifier name, Supplier<String> networkProtocolVersion,
			Predicate<String> clientAcceptedVersions, Predicate<String> serverAcceptedVersions) {
		return new SimpleChannel(name);
	}
}
