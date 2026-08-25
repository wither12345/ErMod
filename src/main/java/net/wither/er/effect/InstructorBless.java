package net.wither.er.effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class InstructorBless extends MobEffect {
    public InstructorBless() {
        super(MobEffectCategory.BENEFICIAL, 0xffff00);
        this.addAttributeModifier(ErModAttributes.ELEMENTAL_MASTERY.get(), "BA7C33DF-6A40-07D7-4D6A-B87AA6916BFF", 120, AttributeModifier.Operation.ADDITION);
    }
}
