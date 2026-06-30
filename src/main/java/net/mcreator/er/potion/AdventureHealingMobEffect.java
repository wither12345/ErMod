package net.mcreator.er.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class AdventureHealingMobEffect extends MobEffect {
	public AdventureHealingMobEffect() {
		super(MobEffectCategory.NEUTRAL, -6684877);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}