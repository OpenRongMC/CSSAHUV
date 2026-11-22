package net.minecraftforge.common.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.ServerLevel;

/**
 * Provides cached {@link FakePlayer} instances, mirroring Forge's {@code FakePlayerFactory}.
 */
public final class FakePlayerFactory {
	private static final GameProfile MINECRAFT_PROFILE = new GameProfile(
			UUID.fromString("41C82C87-7AFB-4024-BA57-13D2C99CAE77"), "[Minecraft]");

	private static final Map<String, FakePlayer> BY_PROFILE = new ConcurrentHashMap<>();

	private FakePlayerFactory() {
	}

	public static FakePlayer getMinecraft(ServerLevel level) {
		return get(level, MINECRAFT_PROFILE);
	}

	public static FakePlayer get(ServerLevel level, GameProfile username) {
		return BY_PROFILE.computeIfAbsent(username.name(), n -> new FakePlayer(level, username));
	}
}
