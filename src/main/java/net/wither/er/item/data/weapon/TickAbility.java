package net.wither.er.item.data.weapon;

import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface TickAbility {
    void tick(LivingEntity entity, int level);
}
