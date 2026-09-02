package net.wither.er.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.common.util.TriState;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static net.wither.er.elements.Element.Category.*;

public class ElementalFarmBlock extends FarmBlock {
    public static final EnumProperty<Element.Category> ELEMENT = EnumProperty.create("element", Element.Category.class);
    private static final EnumSet<Element.Category> FERTILE_ELEMENT = EnumSet.of(ELECTRO, ANEMO, DENDRO);
    public ElementalFarmBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ELEMENT, GEO));
    }

    public static Element.Category getCategory(BlockState blockState) {
        return blockState.getValue(ELEMENT);
    }

    @Override
    public boolean isFertile(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        Element.Category category = state.getValue(ELEMENT);
        return (FERTILE_ELEMENT.contains(category) && super.isFertile(state, level, pos) || category == HYDRO);
    }

    @Override
    public @NotNull TriState canSustainPlant(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos soilPosition, @NotNull Direction facing, @NotNull BlockState plant) {
        if(plant.getBlock() == Blocks.NETHER_WART && state.getBlock() instanceof ElementalFarmBlock && state.getValue(ELEMENT) == PYRO)
            return TriState.TRUE;
        return super.canSustainPlant(state, level, soilPosition, facing, plant);
    }

    @Override
    protected void randomTick(BlockState state, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
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

    @Override
    public void fallOn(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockPos blockPos, @NotNull Entity entity, float v) {
        if(getCategory(blockState) != GEO)
            super.fallOn(level, blockState, blockPos, entity, v);
    }
}
