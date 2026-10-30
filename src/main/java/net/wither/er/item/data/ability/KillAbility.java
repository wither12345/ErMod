package net.wither.er.item.data.ability;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface KillAbility {
    void onKill(DamageSource source, LivingEntity entity, int level);
}
