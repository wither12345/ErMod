package net.wither.er.mixins;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.wither.er.combat.DamageModifierInterface;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DamageSource.class)
public class DamageSourceMixin implements ElementSourceInterface , DamageModifierInterface {
    @Unique
    private ElementSource source ;
    @Unique
    private EntityHurtEvent.DamageModifier modifier = new EntityHurtEvent.DamageModifier();

    @Override
    public ElementSource er$getSource() {
        return source;
    }

    @Override
    public ElementSourceInterface er$setElement(@NotNull ElementSource source) {
        this.source = source ;
        return this ;
    }

    @Override
    public EntityHurtEvent.DamageModifier getModifier() {
        return modifier;
    }
}
