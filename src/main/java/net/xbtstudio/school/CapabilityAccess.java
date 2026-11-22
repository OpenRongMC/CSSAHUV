package net.xbtstudio.school;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.xbtstudio.school.network.SchoolModVariables;

public final class CapabilityAccess {
	private static final Map<UUID, SchoolModVariables.PlayerVariables> PLAYER_VARS = new ConcurrentHashMap<>();

	private CapabilityAccess() {
	}

	public static SchoolModVariables.PlayerVariables playerVars(Entity entity) {
		if (!(entity instanceof Player player)) {
			return new SchoolModVariables.PlayerVariables();
		}
		return PLAYER_VARS.computeIfAbsent(player.getUUID(), id -> new SchoolModVariables.PlayerVariables());
	}

	@SuppressWarnings("unchecked")
	public static <T> LazyOptional<T> get(Entity entity, Capability<T> cap, Direction side) {
		if (cap == SchoolModVariables.PLAYER_VARIABLES_CAPABILITY) {
			return (LazyOptional<T>) LazyOptional.of(() -> playerVars(entity));
		}
		return LazyOptional.empty();
	}

	@SuppressWarnings("unchecked")
	public static <T> LazyOptional<T> get(BlockEntity be, Capability<T> cap, Direction side) {
		return LazyOptional.empty();
	}

	@SuppressWarnings("unchecked")
	public static <T> LazyOptional<T> get(ItemStack stack, Capability<T> cap, Direction side) {
		return LazyOptional.empty();
	}
}
