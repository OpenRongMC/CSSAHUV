
package net.xbtstudio.school.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class ClassroomdoorsBlock extends DoorBlock {
	public ClassroomdoorsBlock() {
		super(BlockSetType.STONE, BlockBehaviour.Properties.of().setId(net.minecraftforge.registries.DeferredRegister.currentBlockKey()).instrument(NoteBlockInstrument.HAT).sound(SoundType.GLASS).strength(1f, 10f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
	}
}
