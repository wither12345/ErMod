package net.wither.er.block;

import com.mojang.serialization.MapCodec;
import net.mcreator.er.init.ErModBlocks;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.wither.er.block.entity.WhopperflowerCropEntity;
import net.wither.er.elements.Element;
import net.wither.er.entity.whopperflower.Whopperflower;
import net.wither.er.init.AdvancementTriggerRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class WhopperflowerCrop extends BushBlock implements EntityBlock, BonemealableBlock{
    public static final MapCodec<WhopperflowerCrop> CODEC = simpleCodec(WhopperflowerCrop::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(7, 0, 7, 9, 2, 9), 
            Block.box(7, 0, 7, 9, 4, 9), 
            Block.box(7, 0, 7, 9, 6, 9), 
            Block.box(7, 0, 7, 9, 8, 9), 
            Block.box(7, 0, 7, 9, 10, 9), 
            Block.box(7, 0, 7, 9, 12, 9), 
            Block.box(7, 0, 7, 9, 14, 9), 
            Block.box(7, 0, 7, 9, 15, 9),
            Block.box(7, 0, 7, 9, 16, 9)};
    ;
    private static final EnumMap<Element.Category, DeferredHolder<EntityType<?>, ? extends EntityType<? extends Whopperflower>>> AVAILABLE_CATEGORY = new EnumMap<>(Map.of(
            Element.Category.PYRO, ErModEntities.PYRO_WHOPPERFLOWER,
            Element.Category.CRYO, ErModEntities.CRYO_WHOPPERFLOWER
    ));

    public WhopperflowerCrop(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }
    
    protected @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        Vec3 vec3 = blockState.getOffset(blockGetter, blockPos);
        return SHAPE_BY_AGE[blockState.getValue(AGE) / 2].move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return blockState.getBlock() instanceof ElementalFarmBlock && AVAILABLE_CATEGORY.containsKey(ElementalFarmBlock.getCategory(blockState));
    }

    protected void randomTick(@NotNull BlockState blockState, ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (serverLevel.isAreaLoaded(blockPos, 1)) {
            if (serverLevel.getRawBrightness(blockPos, 0) >= 9) {
                float f = WhopperflowerCrop.getGrowthSpeed(blockState, serverLevel, blockPos);
                if (CommonHooks.canCropGrow(serverLevel, blockPos, blockState, randomSource.nextInt((int)(25.0F / f) + 1) == 0)) {
                    int i = blockState.getValue(AGE);
                    if (i < 15) {
                        serverLevel.setBlock(blockPos, blockState.setValue(AGE, i + 1), 2);
                    } else{
                        this.spawnWhopperflower(serverLevel, blockPos);
                    }

                    CommonHooks.fireCropGrowPost(serverLevel, blockPos, blockState);
                }
            }

        }
    }

    private void spawnWhopperflower(ServerLevel serverLevel, BlockPos blockPos){
        BlockState farmState = serverLevel.getBlockState(blockPos.below());
        if(farmState.getBlock() instanceof ElementalFarmBlock && AVAILABLE_CATEGORY.containsKey(ElementalFarmBlock.getCategory(farmState))){
            EntityType<? extends Whopperflower> type = AVAILABLE_CATEGORY.get(ElementalFarmBlock.getCategory(farmState)).value();
            Whopperflower flower = type.create(serverLevel);

            if(flower != null) {
                flower.setPos(blockPos.getBottomCenter());
                flower.setDisguisedBlock(ErModBlocks.SWEET_FLOWER.get().defaultBlockState());
                if(serverLevel.getBlockEntity(blockPos) instanceof WhopperflowerCropEntity whopperflowerCropEntity) {
                    if(whopperflowerCropEntity.getOwner() instanceof ServerPlayer player)
                        AdvancementTriggerRegister.WHOPPERFLOWER.get().trigger(player);
                    flower.setOwner(whopperflowerCropEntity.getOwner());
                }
                serverLevel.addFreshEntity(flower);
                serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }

    public boolean isValidBonemealTarget(@NotNull LevelReader levelReader, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        int i = Math.min(15, blockState.getValue(AGE) + Mth.nextInt(serverLevel.random, 2, 5));
        BlockState blockstate = blockState.setValue(AGE, i);
        serverLevel.setBlock(blockPos, blockstate, 2);
        if (i == 15) {
            this.spawnWhopperflower(serverLevel, blockPos);
        }
    }

    @Override
    protected @NotNull MapCodec<WhopperflowerCrop> codec() {
        return CODEC;
    }

    protected static float getGrowthSpeed(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        Block block = blockState.getBlock();
        float f = 1.0F;
        BlockPos blockpos = blockPos.below();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float f1;
                label77: {
                    f1 = 0.0F;
                    BlockState blockstate = blockGetter.getBlockState(blockpos.offset(i, 0, j));
                    TriState soilDecision = blockstate.canSustainPlant(blockGetter, blockpos.offset(i, 0, j), Direction.UP, blockstate);
                    if (soilDecision.isDefault()) {
                        if (!(blockstate.getBlock() instanceof FarmBlock)) {
                            break label77;
                        }
                    } else if (!soilDecision.isTrue()) {
                        break label77;
                    }

                    f1 = 1.0F;
                    if (blockstate.isFertile(blockGetter, blockPos.offset(i, 0, j))) {
                        f1 = 3.0F;
                    }
                    if(blockstate.getBlock() instanceof ElementalFarmBlock){
                        f1 = switch (ElementalFarmBlock.getCategory(blockstate)){
                            case ELECTRO, CRYO, PYRO -> 6;
                            case DENDRO, GEO, HYDRO -> 4;
                            case ANEMO -> 1;
                        };
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockPos1 = blockPos.north();
        BlockPos blockPos2 = blockPos.south();
        BlockPos blockPos3 = blockPos.west();
        BlockPos blockPos4 = blockPos.east();
        boolean flag = blockGetter.getBlockState(blockPos3).is(block) || blockGetter.getBlockState(blockPos4).is(block);
        boolean flag1 = blockGetter.getBlockState(blockPos1).is(block) || blockGetter.getBlockState(blockPos2).is(block);
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = blockGetter.getBlockState(blockPos3.north()).is(block) || blockGetter.getBlockState(blockPos4.north()).is(block) || blockGetter.getBlockState(blockPos4.south()).is(block) || blockGetter.getBlockState(blockPos3.south()).is(block);
            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new WhopperflowerCropEntity(blockPos, blockState);
    }
}
