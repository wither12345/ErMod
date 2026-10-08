package net.wither.er.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class Determination extends MobEffect {
    public Determination() {
        super(MobEffectCategory.BENEFICIAL, 0xbbffbb);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "7C32B354-DF69-6FF2-8EE7-3BCBB2F93C32", 0.12, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amp, @NotNull AttributeModifier modifier) {
        return modifier.getAmount() + 0.03;
    }
}
