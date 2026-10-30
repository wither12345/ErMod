package net.wither.er.item.data.ability;

import net.wither.er.item.fortuna.StellaFortuna;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface OnBurstAbility {
    void onBurst(LivingEntity entity, StellaFortuna stellaFortuna, int level);
}
