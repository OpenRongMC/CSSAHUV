package net.minecraftforge.client.event;

import java.util.List;

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;

public abstract class RegisterColorHandlersEvent extends Event {
	public static class Block extends RegisterColorHandlersEvent {
		private final BlockColorsProxy colors = new BlockColorsProxy();

		public void register(BlockColorCompat color, net.minecraft.world.level.block.Block... blocks) {
			this.colors.register(color, blocks);
		}

		public BlockColorsProxy getBlockColors() {
			return this.colors;
		}
	}

	public static class Item extends RegisterColorHandlersEvent {
	}

	public static final class BlockColorsProxy {
		public void register(BlockColorCompat color, net.minecraft.world.level.block.Block... blocks) {
			BlockTintSource source = new BlockTintSource() {
				@Override
				public int color(BlockState state) {
					return color.getColor(state, null, null, 0);
				}

				@Override
				public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
					return color.getColor(state, level, pos, 0);
				}
			};
			BlockColorRegistry.register(List.of(source), blocks);
		}
	}
}
