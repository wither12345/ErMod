package net.wither.er.item.data.weapon;

import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface HealthFloatingAbility {
    void onFloat(LivingEntity entity, float delta, int level);
}
