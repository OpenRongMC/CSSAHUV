
package net.xbtstudio.school.block;


import net.xbtstudio.school.procedures.AlcoholLampExplodeProcedure;
import net.xbtstudio.school.procedures.AlcoholLampDestroyProcedure;
import net.xbtstudio.school.procedures.AlcoholLampBombProcedure;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class AlcoholLampBlock extends Block implements SimpleWaterloggedBlock {
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public AlcoholLampBlock() {
		super(BlockBehaviour.Properties.of().setId(net.minecraftforge.registries.DeferredRegister.currentBlockKey()).instrument(NoteBlockInstrument.HAT).sound(SoundType.GLASS).strength(0.85f, 10f).lightLevel(s -> 5).noCollision().noOcclusion().pushReaction(PushReaction.POPPED)
				.emissiveRendering(bs -> true).isRedstoneConductor((bs, br, bp) -> false).offsetType(Block.OffsetType.XZ));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state) {
		return state.getFluidState().isEmpty();
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Vec3 offset = state.getOffset(pos);
		return (switch (state.getValue(FACING)) {
			default -> Shapes.or(box(5.5, 0.5, 5.5, 10.5, 2.75, 10.5), box(6, 0, 6, 10, 0.5, 10), box(7.25, 2.75, 7.25, 8.75, 3.75, 8.75), box(7.5, 3.75, 7.5, 8.5, 4.25, 8.5), box(5.75, 0.75, 5.75, 10.25, 1.75, 10.25),
					box(7.75, 0.75, 7.75, 8.25, 3.65, 8.25));
			case NORTH -> Shapes.or(box(5.5, 0.5, 5.5, 10.5, 2.75, 10.5), box(6, 0, 6, 10, 0.5, 10), box(7.25, 2.75, 7.25, 8.75, 3.75, 8.75), box(7.5, 3.75, 7.5, 8.5, 4.25, 8.5), box(5.75, 0.75, 5.75, 10.25, 1.75, 10.25),
					box(7.75, 0.75, 7.75, 8.25, 3.65, 8.25));
			case EAST -> Shapes.or(box(5.5, 0.5, 5.5, 10.5, 2.75, 10.5), box(6, 0, 6, 10, 0.5, 10), box(7.25, 2.75, 7.25, 8.75, 3.75, 8.75), box(7.5, 3.75, 7.5, 8.5, 4.25, 8.5), box(5.75, 0.75, 5.75, 10.25, 1.75, 10.25),
					box(7.75, 0.75, 7.75, 8.25, 3.65, 8.25));
			case WEST -> Shapes.or(box(5.5, 0.5, 5.5, 10.5, 2.75, 10.5), box(6, 0, 6, 10, 0.5, 10), box(7.25, 2.75, 7.25, 8.75, 3.75, 8.75), box(7.5, 3.75, 7.5, 8.5, 4.25, 8.5), box(5.75, 0.75, 5.75, 10.25, 1.75, 10.25),
					box(7.75, 0.75, 7.75, 8.25, 3.65, 8.25));
		}).move(offset.x, offset.y, offset.z);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, flag);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return this.rotate(state, mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState updateShape(BlockState state, net.minecraft.world.level.LevelReader world, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, net.minecraft.util.RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			ticks.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, world, ticks, currentPos, facing, facingPos, facingState, random);
	}

	@Override
	public void wasExploded(net.minecraft.server.level.ServerLevel world, BlockPos pos, Explosion e) {
		super.wasExploded(world, pos, e);
		AlcoholLampBombProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void onProjectileHit(Level world, BlockState blockstate, BlockHitResult hit, Projectile entity) {
		AlcoholLampDestroyProcedure.execute(world, hit.getBlockPos().getX(), hit.getBlockPos().getY(), hit.getBlockPos().getZ());
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level world, BlockPos pos, Player entity, BlockHitResult hit) {
		super.useWithoutItem(blockstate, world, pos, entity, hit);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		double hitX = hit.getLocation().x;
		double hitY = hit.getLocation().y;
		double hitZ = hit.getLocation().z;
		Direction direction = hit.getDirection();
		AlcoholLampExplodeProcedure.execute(world, x, y, z, entity);
		return InteractionResult.SUCCESS;
	}
}
