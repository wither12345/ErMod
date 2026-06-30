package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

public class Quicken extends Element{

    @Override
    public Category getCategory() {
        return Category.DENDRO;
    }

    @Override
    public float reactWith(AuraContainer container, SingleElementalContainer singleElementalContainer, float strength, LevelAccessor accessor, double x, double y, double z, int level, double elemental_mastery, @Nullable EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
        return 0 ;
    }

    @Override
    public boolean overrideReduceRate() {
        return true;
    }

    @Override
    public float getReduceRate(float gauge) {
        return 1/(gauge * 5 + 6) ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.QUICKEN;
    }
}
