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
import net.minecraft.world.item.Item;
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
import net.minecraftforge.items.ItemHandlerHelper;
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
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
	public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
	}

	@Override
	public @NotNull InteractionResult use(@NotNull BlockState blockstate, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player entity, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		super.use(blockstate, world, pos, entity, hand, hit);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
        if (!getBlockNBTLogic(world, BlockPos.containing(x, y, z), "active")) {
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
            entity.getCapability(ErItemVariables.PLAYER_VARIABLES).ifPresent(vars -> {
                if(vars.Stella_Fortuna.is(ErModItems.EMPTY_FORTUNA.get())){
                    vars.Stella_Fortuna = transmuteCopy(vars.Stella_Fortuna, ErModItems.MEMORY_OF_ROVING_GALES.get());
                    vars.syncToClient(entity);
                }
                else if(entity.getMainHandItem().is(ErModItems.EMPTY_FORTUNA.get()))
                    entity.setItemInHand(InteractionHand.MAIN_HAND, transmuteCopy(entity.getMainHandItem(), ErModItems.MEMORY_OF_ROVING_GALES.get()));
                else if(entity.getOffhandItem().is(ErModItems.EMPTY_FORTUNA.get()))
                    entity.setItemInHand(InteractionHand.OFF_HAND, transmuteCopy(entity.getOffhandItem(), ErModItems.MEMORY_OF_ROVING_GALES.get()));
                else
                    ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ErModItems.MEMORY_OF_ROVING_GALES.get()));
            });
        }
        if (entity.getMainHandItem().is(ErModItems.LEY_LINE_MAP.get()))
            LeyLineMapItem.putWaypoint(entity.getMainHandItem(), entity.level(), x, y, z, true);
		return InteractionResult.SUCCESS;
	}

    private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null)
            return blockEntity.getPersistentData().getBoolean(tag);
        return false;
    }

    private static ItemStack transmuteCopy(ItemStack stack, Item item) {
        ItemStack newStack = new ItemStack(item);
        newStack.setTag(stack.getTag());
        return newStack;
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