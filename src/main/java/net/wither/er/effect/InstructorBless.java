package net.wither.er.effect;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class InstructorBless extends MobEffect {
    public InstructorBless() {
        super(MobEffectCategory.BENEFICIAL, 0xffff00);
        this.addAttributeModifier(ErModAttributes.ELEMENTAL_MASTERY, ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "instructor"), 120, AttributeModifier.Operation.ADD_VALUE);
    }
}
