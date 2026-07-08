package net.wither.er.item.data.weapon;

import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface OnBurstAbility {
    void onBurst(LivingEntity entity, int level);
}
