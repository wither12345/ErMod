package net.mcreator.er.block;

import net.mcreator.er.block.entity.StatueofTheSevenCoreBlockEntity;
import net.mcreator.er.init.ErModItems;
import net.mcreator.er.item.LeyLineMapItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.wither.er.network.ErItemVariables;
import org.jetbrains.annotations.NotNull;

public class StatueofTheSevenCoreBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public StatueofTheSevenCoreBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(-1, 3600000).lightLevel(blockstate -> 15).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	public int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
		return propagatesSkylightDown(state, worldIn, pos) ? 0 : 1;
	}

	@Override
	public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		return super.getStateForPlacement(context).setValue(WATERLOGGED, flag);
	}

	@Override
	public @NotNull FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
	}

	@Override
	public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockstate, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player entity, @NotNull BlockHitResult hit) {
		super.useWithoutItem(blockstate, world, pos, entity, hit);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

        if (!getBlockNBTLogic(world, BlockPos.containing(x, y, z))) {
            if (!world.isClientSide()) {
                BlockPos _bp = BlockPos.containing(x, y, z);
                BlockEntity _blockEntity = world.getBlockEntity(_bp);
                BlockState _bs = world.getBlockState(_bp);
                if (_blockEntity != null) {
                    _blockEntity.getPersistentData().putBoolean("active", true);
                    _blockEntity.setChanged();
                }
                world.sendBlockUpdated(_bp, _bs, _bs, 3);
            }
            ErItemVariables.PlayerVariables var = entity.getData(ErItemVariables.PLAYER_VARIABLES);
            if(var.Stella_Fortuna.is(ErModItems.EMPTY_FORTUNA)){
                var.Stella_Fortuna = var.Stella_Fortuna.transmuteCopy(ErModItems.MEMORYOF_ROVING_GALES);
                var.syncPlayerVariables(entity);
            }
            else if(entity.getMainHandItem().is(ErModItems.EMPTY_FORTUNA))
                entity.setItemInHand(InteractionHand.MAIN_HAND, entity.getMainHandItem().transmuteCopy(ErModItems.MEMORYOF_ROVING_GALES));
            else if(entity.getOffhandItem().is(ErModItems.EMPTY_FORTUNA))
                entity.setItemInHand(InteractionHand.OFF_HAND, entity.getOffhandItem().transmuteCopy(ErModItems.MEMORYOF_ROVING_GALES));
            else
                ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ErModItems.MEMORYOF_ROVING_GALES.get()));
        }
        if (entity.getMainHandItem().is(ErModItems.LEY_LINE_MAP)) {
            LeyLineMapItem.putWaypoint(entity.getMainHandItem(), entity.level(), x, y, z, true);
        }
		return InteractionResult.SUCCESS;
	}

    private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null)
            return blockEntity.getPersistentData().getBoolean("active");
        return false;
    }

	@Override
	public MenuProvider getMenuProvider(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new StatueofTheSevenCoreBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
	}

	@Override
	public void onRemove(BlockState state, @NotNull Level world, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof StatueofTheSevenCoreBlockEntity be) {
				Containers.dropContents(world, pos, be);
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(@NotNull BlockState blockState, Level world, @NotNull BlockPos pos) {
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof StatueofTheSevenCoreBlockEntity be)
			return AbstractContainerMenu.getRedstoneSignalFromContainer(be);
		else
			return 0;
	}
}