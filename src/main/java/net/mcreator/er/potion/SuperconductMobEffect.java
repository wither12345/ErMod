package net.mcreator.er.potion;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.ErMod;

public class SuperconductMobEffect extends MobEffect {
	public SuperconductMobEffect() {
		super(MobEffectCategory.HARMFUL, -6749953);
		this.addAttributeModifier(ErModAttributes.PHYSICAL_RES, ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "effect.superconduct_0"), -40, AttributeModifier.Operation.ADD_VALUE);
	}
}