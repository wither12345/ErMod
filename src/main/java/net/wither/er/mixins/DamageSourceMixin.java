package net.wither.er.mixins;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.wither.er.combat.DamageModifierInterface;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DamageSource.class)
public class DamageSourceMixin implements ElementSourceInterface , DamageModifierInterface {
    @Unique
    private ElementSource er$source;
    @Unique
    private EntityHurtEvent.DamageModifier er$modifier = new EntityHurtEvent.DamageModifier();

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
    public EntityHurtEvent.DamageModifier getModifier() {
        return er$modifier;
    }
}
