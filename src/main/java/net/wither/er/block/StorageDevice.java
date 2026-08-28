package net.wither.er.block;

import net.mcreator.er.init.ErModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.VanillaInventoryCodeHooks;
import net.wither.er.block.entity.StorageDeviceEntity;
import org.jetbrains.annotations.NotNull;

public class StorageDevice extends DispenserBlock implements SimpleWaterloggedBlock {
    private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new DefaultDispenseItemBehavior();
    private static final VoxelShape SHAPE = box(2, 0, 2, 14, 20, 14);

    public StorageDevice() {
        super(Properties.of()
                .mapColor(MapColor.ICE)
                .strength(0.5F)
                .sound(SoundType.GLASS)
                .noOcclusion()
                );
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new StorageDeviceEntity(blockPos, blockState);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(BlockStateProperties.WATERLOGGED, flag);
    }

    protected void dispenseFrom(@NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos) {
        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(serverLevel, blockPos);
        StorageDeviceEntity deviceEntity = blocksourceimpl.getEntity();
        int i = deviceEntity.getRandomSlot(serverLevel.random);
        if (i < 0) {
            serverLevel.levelEvent(1001, blockPos, 0);
        } else {
            ItemStack itemstack = deviceEntity.getItem(i);
            if (!itemstack.isEmpty() && VanillaInventoryCodeHooks.dropperInsertHook(serverLevel, blockPos, deviceEntity, i, itemstack)) {
                Direction direction = serverLevel.getBlockState(blockPos).getValue(FACING);
                Container container = HopperBlockEntity.getContainerAt(serverLevel, blockPos.relative(direction));
                ItemStack itemstack1;
                if (container == null) {
                    itemstack1 = DISPENSE_BEHAVIOUR.dispense(blocksourceimpl, itemstack);
                } else {
                    itemstack1 = HopperBlockEntity.addItem(deviceEntity, container, itemstack.copy().split(1), direction.getOpposite());
                    if (itemstack1.isEmpty()) {
                        itemstack1 = itemstack.copy();
                        itemstack1.shrink(1);
                    } else {
                        itemstack1 = itemstack.copy();
                    }
                }

                deviceEntity.setItem(i, itemstack1);
            }
        }

    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean flag) {
        if(!state.is(blockState.getBlock()) &&
                level.getBlockEntity(blockPos) instanceof StorageDeviceEntity deviceEntity &&
                level instanceof ServerLevel serverLevel){
            ItemStack itemstack = new ItemStack(ErModItems.STORAGE_DEVICE.get());
            if(!deviceEntity.isEmpty())
                deviceEntity.saveToItem(itemstack);
            if (deviceEntity.hasCustomName()) {
                itemstack.setHoverName(deviceEntity.getCustomName());
            }
            ItemEntity itementity = new ItemEntity(serverLevel, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, itemstack);
            itementity.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(itementity);
        }
        if (state.hasBlockEntity() && !state.is(blockState.getBlock())) {
            level.removeBlockEntity(blockPos);
        }
    }
}

