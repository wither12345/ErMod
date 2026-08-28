package net.wither.er.block;

import com.mojang.logging.LogUtils;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.VanillaInventoryCodeHooks;
import net.wither.er.block.entity.StorageDeviceEntity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class StorageDevice extends DispenserBlock implements SimpleWaterloggedBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new DefaultDispenseItemBehavior();
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = box(2, 0, 2, 14, 20, 14);

    public StorageDevice() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.ICE)
                .strength(0.5F)
                .sound(SoundType.GLASS)
                .noOcclusion()
                );
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    protected @NotNull DispenseItemBehavior getDispenseMethod(@NotNull Level level, @NotNull ItemStack itemStack) {
        return DISPENSE_BEHAVIOUR;
    }

    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new StorageDeviceEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, flag);
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

    protected void dispenseFrom(ServerLevel serverLevel, @NotNull BlockState blockState, @NotNull BlockPos blockPos) {
        if(serverLevel.getBlockEntity(blockPos) instanceof StorageDeviceEntity deviceEntity){
            BlockSource blocksource = new BlockSource(serverLevel, blockPos, blockState, deviceEntity);
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
                        itemstack1 = DISPENSE_BEHAVIOUR.dispense(blocksource, itemstack);
                    } else {
                        itemstack1 = HopperBlockEntity.addItem(deviceEntity, container, itemstack.copyWithCount(1), direction.getOpposite());
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
        else {
            LOGGER.warn("Ignoring dispensing attempt for Dropper without matching block entity at {}", blockPos);
        }
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean flag) {
        if(!state.is(blockState.getBlock()) &&
                level.getBlockEntity(blockPos) instanceof StorageDeviceEntity deviceEntity &&
                level instanceof ServerLevel serverLevel){
            ItemStack itemstack = new ItemStack(ErModItems.STORAGE_DEVICE.get());
            itemstack.applyComponents(deviceEntity.collectComponents());
            ItemEntity itementity = new ItemEntity(serverLevel, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, itemstack);
            itementity.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(itementity);
        }
        if (state.hasBlockEntity() && !state.is(blockState.getBlock())) {
            level.removeBlockEntity(blockPos);
        }
    }
}

