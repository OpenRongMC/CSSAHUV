
package net.xbtstudio.school.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class SidewalkBlock extends Block {
	public SidewalkBlock() {
		super(BlockBehaviour.Properties.of().setId(net.minecraftforge.registries.DeferredRegister.currentBlockKey()).sound(SoundType.STONE).strength(1f, 10f));
	}
}
