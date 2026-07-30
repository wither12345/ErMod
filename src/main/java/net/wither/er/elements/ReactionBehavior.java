package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ReactionBehavior {
    float reactWith(AuraContainer auraContainer, Element self, ElementalAura boundAura, ElementSource source, EntityHurtEvent.DamageModifier modifier, @Nullable Entity applier);}
