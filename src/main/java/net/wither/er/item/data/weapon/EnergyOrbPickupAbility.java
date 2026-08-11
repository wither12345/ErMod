package net.wither.er.item.data.weapon;

import net.minecraft.world.entity.LivingEntity;
import net.wither.er.entity.EnergyOrb;

@FunctionalInterface
public interface EnergyOrbPickupAbility {
    void onPick(EnergyOrb orb, LivingEntity picker, int level);
}
