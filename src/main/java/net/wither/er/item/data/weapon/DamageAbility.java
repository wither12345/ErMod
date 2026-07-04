package net.wither.er.item.data.weapon;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface DamageAbility {
    void onHurt(DamageSource source, LivingEntity entity, EntityHurtEvent.DamageModifier modifier, int level);
}
