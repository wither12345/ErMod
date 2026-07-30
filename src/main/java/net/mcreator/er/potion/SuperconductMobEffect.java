package net.mcreator.er.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.init.ErAttributeRegister;

public class SuperconductMobEffect extends MobEffect {
	public SuperconductMobEffect() {
		super(MobEffectCategory.HARMFUL, -6749953);
		this.addAttributeModifier(ErAttributeRegister.PHYSICAL_RES.get(), "0560e952-caea-3329-bcee-233dfccc950e", -40, AttributeModifier.Operation.ADDITION);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}