package net.wither.er.block;

import net.mcreator.er.init.ErModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.wither.er.block.entity.LinkMechanismBaseEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LinkMechanismBase extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public LinkMechanismBase() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.GLASS)
                .strength(0.5f)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false)
                .isSuffocating((bs, br, bp) -> false));
        this.registerDefaultState(this.stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.EXTENDED, false));
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.PERSISTENT, false));
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
    }

    public static Direction next(Direction direction){
        return switch (direction){
            case EAST -> Direction.NORTH;
            case NORTH -> Direction.WEST;
            case WEST -> Direction.SOUTH;
            default -> Direction.EAST;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING);
        builder.add(BlockStateProperties.EXTENDED);
        builder.add(BlockStateProperties.PERSISTENT);
        builder.add(WATERLOGGED);
    }


    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;

        return super.getStateForPlacement(context)
                .setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection())
                .setValue(BlockStateProperties.EXTENDED, false)
                .setValue(BlockStateProperties.PERSISTENT, true)
                .setValue(WATERLOGGED, flag);
    }
    
    public static Vec3 getCenter(BlockPos pos, Direction direction){
        return switch (direction){
            case WEST -> pos.getCenter().add(-0.5, 2.5, 0.5);
            case NORTH -> pos.getCenter().add(-0.5, 2.5, -0.5);
            case EAST -> pos.getCenter().add(0.5, 2.5, -0.5);
            default -> pos.getCenter().add(0.5, 2.5, 0.5);
        };
    }

    @Override
    public void destroy(@NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        super.destroy(levelAccessor, blockPos, blockState);
        Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos newPos = blockPos;
        for(int i = 0; i < 3; i ++) {
            newPos = newPos.relative(direction);
            direction = next(direction);
            if (levelAccessor.getBlockState(newPos).is(ErModBlocks.LINK_MECHANISM_BASE))
                levelAccessor.destroyBlock(newPos, false);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new LinkMechanismBaseEntity(blockPos, blockState);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return box(0, 0, 0, 16, 15, 16);
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }


}
