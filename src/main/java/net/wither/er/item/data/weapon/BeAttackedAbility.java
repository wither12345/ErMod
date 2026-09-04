package net.wither.er.item.data.weapon;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface BeAttackedAbility {
    void beAttacked(LivingEntity self, DamageSource source, EntityHurtEvent.DamageModifier modifier, float dmgAmount, int level);
}
