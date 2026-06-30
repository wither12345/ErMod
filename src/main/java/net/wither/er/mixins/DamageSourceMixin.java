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
    private ElementSource source ;
    @Unique
    private EntityHurtEvent.DamageModifier modifier = new EntityHurtEvent.DamageModifier();

    @Override
    public ElementSource getSource() {
        return source;
    }

    @Override
    public ElementSourceInterface setElement(ElementSource source) {
        this.source = source ;
        return this ;
    }

    @Override
    public EntityHurtEvent.DamageModifier getModifier() {
        return modifier;
    }
}
