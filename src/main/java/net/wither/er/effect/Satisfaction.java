package net.wither.er.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class Satisfaction extends MobEffect {
    public Satisfaction() {
        super(MobEffectCategory.BENEFICIAL, 0x44ff44);
    }


    @Override
    public boolean isDurationEffectTick(int tick, int amp) {
        return tick % (amp == 0 ? 160 : 100) == 0;
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity living, int amp) {
        living.heal(amp == 0 ? 1 : (1.5f + amp * 2.5f));
    }
}
