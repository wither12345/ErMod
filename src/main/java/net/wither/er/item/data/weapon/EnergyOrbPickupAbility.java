package net.wither.er.item.data.weapon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface EnergyOrbPickupAbility {
    void onPick(Entity orb, LivingEntity picker, int level);
}
