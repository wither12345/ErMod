package net.wither.er.block;

import net.mcreator.er.init.ErModBlocks;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.block.entity.WhopperflowerCropEntity;
import net.wither.er.elements.Element;
import net.wither.er.entity.whopperflower.Whopperflower;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class WhopperflowerCrop extends BushBlock implements EntityBlock, BonemealableBlock{
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
    private static final EnumMap<Element.Category, RegistryObject<? extends EntityType<? extends Whopperflower>>> AVAILABLE_CATEGORY = new EnumMap<>(Map.of(
            Element.Category.PYRO, ErModEntities.PYRO_WHOPPERFLOWER,
            Element.Category.CRYO, ErModEntities.CRYO_WHOPPERFLOWER
    ));

    public WhopperflowerCrop(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }
    
    public @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        Vec3 vec3 = blockState.getOffset(blockGetter, blockPos);
        return SHAPE_BY_AGE[blockState.getValue(AGE) / 2].move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return blockState.getBlock() instanceof ElementalFarmBlock && AVAILABLE_CATEGORY.containsKey(ElementalFarmBlock.getCategory(blockState));
    }

    @Override
    public boolean canSurvive(@NotNull BlockState blockState, @NotNull LevelReader blockGetter, @NotNull BlockPos blockPos) {
        BlockState stateBelow = blockGetter.getBlockState(blockPos.below());
        return stateBelow.getBlock() instanceof ElementalFarmBlock && AVAILABLE_CATEGORY.containsKey(ElementalFarmBlock.getCategory(stateBelow));
    }

    public void randomTick(@NotNull BlockState blockState, ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (serverLevel.isAreaLoaded(blockPos, 1)) {
            if (serverLevel.getRawBrightness(blockPos, 0) >= 9) {
                float f = WhopperflowerCrop.getGrowthSpeed(this, serverLevel, blockPos);
                if (ForgeHooks.onCropsGrowPre(serverLevel, blockPos, blockState, randomSource.nextInt((int)(25.0F / f) + 1) == 0)) {
                    int i = blockState.getValue(AGE);
                    if (i < 15) {
                        serverLevel.setBlock(blockPos, blockState.setValue(AGE, i + 1), 2);
                    } else{
                        this.spawnWhopperflower(serverLevel, blockPos);
                    }

                    ForgeHooks.onCropsGrowPost(serverLevel, blockPos, blockState);
                }
            }

        }
    }

    private void spawnWhopperflower(ServerLevel serverLevel, BlockPos blockPos){
        BlockState farmState = serverLevel.getBlockState(blockPos.below());
        if(farmState.getBlock() instanceof ElementalFarmBlock && AVAILABLE_CATEGORY.containsKey(ElementalFarmBlock.getCategory(farmState))){
            EntityType<? extends Whopperflower> type = AVAILABLE_CATEGORY.get(ElementalFarmBlock.getCategory(farmState)).get();
            Whopperflower flower = type.create(serverLevel);

            if(flower != null) {
                flower.setPos(blockPos.below().getCenter());
                flower.setDisguisedBlock(ErModBlocks.SWEET_FLOWER.get().defaultBlockState());
                if(serverLevel.getBlockEntity(blockPos) instanceof WhopperflowerCropEntity whopperflowerCropEntity) {
                    flower.setOwner(whopperflowerCropEntity.getOwner());
                }
                serverLevel.addFreshEntity(flower);
                serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean b) {
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

    protected static float getGrowthSpeed(Block block, BlockGetter blockGetter, BlockPos blockPos) {
        float f = 1.0F;
        BlockPos blockpos = blockPos.below();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float f1;
                label77: {
                    f1 = 0.0F;
                    BlockState blockstate = blockGetter.getBlockState(blockpos.offset(i, 0, j));
                    if (blockstate.canSustainPlant(blockGetter, blockpos.offset(i, 0, j), Direction.UP, (IPlantable)block)) {
                        f1 = 1.0F;
                        if (blockstate.isFertile(blockGetter, blockpos.offset(i, 0, j))) {
                            f1 = 3.0F;
                        }
                    }

                    f1 = 1.0F;
                    if (blockstate.isFertile(blockGetter, blockPos.offset(i, 0, j))) {
                        f1 = 3.0F;
                    }
                    if(block instanceof ElementalFarmBlock){
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
