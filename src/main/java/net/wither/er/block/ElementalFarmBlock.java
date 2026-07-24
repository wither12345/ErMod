package net.wither.er.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static net.wither.er.elements.Element.Category.*;

public class ElementalFarmBlock extends FarmBlock {
    public static final EnumProperty<Element.Category> ELEMENT = EnumProperty.create("element", Element.Category.class);
    private static final EnumSet<Element.Category> FERTILE_ELEMENT = EnumSet.of(ELECTRO, HYDRO, DENDRO);
    public ElementalFarmBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ELEMENT, GEO));
    }

    public static Element.Category getCategory(BlockState blockState) {
        return blockState.getValue(ELEMENT);
    }

    @Override
    public boolean canSustainPlant(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull Direction facing, @NotNull IPlantable plantable) {
        PlantType type = plantable.getPlantType(world, pos.relative(facing));
        if(type.equals(PlantType.CROP) || type.equals(PlantType.PLAINS))
            return true;
        if(type.equals(PlantType.NETHER) && world.getBlockState(pos).getValue(ELEMENT) == PYRO)
            return true;
        return super.canSustainPlant(state, world, pos, facing, plantable);
    }

    @Override
    public boolean isFertile(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return FERTILE_ELEMENT.contains(state.getValue(ELEMENT)) && super.isFertile(state, level, pos);
    }

    @Override
    public void randomTick(BlockState state, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if(FERTILE_ELEMENT.contains(state.getValue(ELEMENT)))
            super.randomTick(state, serverLevel, blockPos, randomSource);
    }

    public int getFertile(@NotNull BlockState state) {
        return switch (state.getValue(ELEMENT)){
            case ELECTRO, HYDRO, DENDRO -> 2;
            case GEO -> 1;
            case ANEMO -> 0;
            case CRYO, PYRO -> -1;
        };
    }

    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ELEMENT);
    }
}
