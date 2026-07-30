package net.mcreator.er.potion;

import net.mcreator.er.ErMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.init.ErAttributeRegister;

public class SuperconductMobEffect extends MobEffect {
	public SuperconductMobEffect() {
		super(MobEffectCategory.HARMFUL, -6749953);
		this.addAttributeModifier(ErAttributeRegister.PHYSICAL_RES, ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "effect.superconduct_0"), -40, AttributeModifier.Operation.ADD_VALUE);
	}
}