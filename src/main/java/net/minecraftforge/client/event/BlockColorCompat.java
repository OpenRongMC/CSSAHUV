package net.minecraftforge.client.event;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockColorCompat {
	int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex);
}
