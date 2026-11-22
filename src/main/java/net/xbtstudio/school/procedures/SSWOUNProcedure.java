package net.xbtstudio.school.procedures;

import net.xbtstudio.school.init.SchoolModBlocks;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;


public class SSWOUNProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		{
			BlockPos _bp = BlockPos.containing(x, y, z);
			BlockState _bs = SchoolModBlocks.SSWU.get().defaultBlockState();
			BlockState _bso = world.getBlockState(_bp);
			for (Property.Value<?> value : _bso.getValues().toList()) {
				Property _property = _bs.getBlock().getStateDefinition().getProperty(value.property().getName());
				if (_property != null && _bs.getValue(_property) != null)
					try {
						_bs = _bs.setValue(_property, (Comparable) value.value());
					} catch (Exception e) {
					}
			}
			world.setBlock(_bp, _bs, 3);
		}
	}
}
