package net.wither.er.item.data.weapon;

import net.wither.er.item.StellaFortuna;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface OnBurstAbility {
    void onBurst(LivingEntity entity, StellaFortuna fortuna, int level);
}
