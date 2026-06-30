package net.mcreator.er.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public class DisorderOmenMobEffect extends MobEffect {
	public DisorderOmenMobEffect() {
		super(MobEffectCategory.NEUTRAL, -16777216);
		this.withSoundOnAdded(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.elder_guardian.curse")));
	}
}