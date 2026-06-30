package net.wither.er.entity.slimes;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.wither.er.elements.Element;
import net.wither.er.init.ElementRegistry;
import org.jetbrains.annotations.NotNull;

public class CryoSlime extends ElementalSlime{
    public CryoSlime(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    Element getElement() {
        return ElementRegistry.CRYO.get();
    }

    @Override
    public boolean canStandOnFluid(@NotNull FluidState state) {
        return state.is(FluidTags.WATER) || super.canStandOnFluid(state) ;
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos pos = this.getOnPos();
        for(int dx = -2 ; dx <= 2 ; dx ++)
            for(int dy = -1; dy <= 1 ; dy++)
                for(int dz = -2; dz <= 2 ; dz++)
                    if(this.level().getBlockState(pos.offset(dx,dy,dz)).getBlock() == Blocks.WATER && this.level().getBlockState(pos.offset(dx,dy + 1,dz)).is(BlockTags.create(new ResourceLocation("minecraft:air")))){
                        level().setBlock(pos.offset(dx,dy,dz), Blocks.FROSTED_ICE.defaultBlockState(), 3);
                    }
    }

    @Override
    boolean isTiny() {
        return true;
    }
}
