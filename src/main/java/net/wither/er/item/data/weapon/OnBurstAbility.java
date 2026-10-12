package net.wither.er.item.data.weapon;

import net.mcreator.er.StellaFortunas;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface OnBurstAbility {
    void onBurst(LivingEntity entity, StellaFortunas fortuna, int level);
}
