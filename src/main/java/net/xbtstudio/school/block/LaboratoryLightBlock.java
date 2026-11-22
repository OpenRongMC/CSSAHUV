
package net.xbtstudio.school.block;


import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class LaboratoryLightBlock extends SlabBlock {
	public LaboratoryLightBlock() {
		super(BlockBehaviour.Properties.of().setId(net.minecraftforge.registries.DeferredRegister.currentBlockKey()).sound(SoundType.LANTERN).strength(1f, 10f).lightLevel(s -> 15).emissiveRendering(bs -> true));
	}
}
