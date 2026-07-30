package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ReactionBehavior {
    float reactWith(AuraContainer auraContainer, Element self, ElementalAura boundAura, ElementSource source, EntityHurtEvent.DamageModifier modifier, @Nullable Entity applier);
    //float reactWith(AuraContainer container , SingleElementalContainer singleElementalContainer ,float gauge, LevelAccessor accessor , double x , double y , double z, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) ;
}
