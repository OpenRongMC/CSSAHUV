package net.minecraftforge.common.util;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * A server-side player stand-in used for machine/command actions with no real player. Mirrors
 * Forge's {@code FakePlayer} (a lightweight {@link ServerPlayer} subclass).
 */
public class FakePlayer extends ServerPlayer {
	public FakePlayer(ServerLevel level, GameProfile name) {
		super(level.getServer(), level, name, ClientInformation.createDefault());
	}

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public boolean isCreative() {
		return false;
	}
}
