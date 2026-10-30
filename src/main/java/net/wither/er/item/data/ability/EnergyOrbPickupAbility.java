package net.wither.er.item.data.ability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface EnergyOrbPickupAbility {
    void onPick(Entity orb, LivingEntity picker, int level);
}
