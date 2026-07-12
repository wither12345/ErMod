package net.wither.er.mixins;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.wither.er.combat.DamageModifierInterface;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DamageSource.class)
public class DamageSourceMixin implements ElementSourceInterface , DamageModifierInterface {
    @Unique private ElementSource er$source = null;
    @Unique private EntityHurtEvent.DamageModifier er$modifier = new EntityHurtEvent.DamageModifier();
    @Unique private Entity er$target ;
    @Unique private ElementSource er$savedSource = null;

    @Override
    public ElementSource er$getSource() {
        return er$source;
    }

    @Override
    public ElementSourceInterface er$setElement(ElementSource source) {
        this.er$source = source ;
        return this ;
    }

    @Override
    public boolean er$oriEmpty() {
        if(er$savedSource != null) {
            this.er$source = er$savedSource;
            return false;
        }
        if(er$source == null) return true;
        er$savedSource = er$source.copy();
        return false;
    }

    @Override
    public void er$reset() {
        er$modifier = new EntityHurtEvent.DamageModifier();
    }

    @Override
    public EntityHurtEvent.DamageModifier er$getModifier() {
        return er$modifier;
    }

    @Override
    public Entity er$getTarget() {
        return er$target;
    }

    @Override
    public void er$setTarget(Entity entity) {
        this.er$target = entity;
    }
}
