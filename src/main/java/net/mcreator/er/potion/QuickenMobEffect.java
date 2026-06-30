package net.mcreator.er.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class QuickenMobEffect extends MobEffect {
	public QuickenMobEffect() {
		super(MobEffectCategory.HARMFUL, -16724992);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}