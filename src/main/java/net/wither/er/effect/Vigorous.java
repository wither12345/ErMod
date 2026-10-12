package net.wither.er.effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class Vigorous extends MobEffect {
    public Vigorous() {
        super(MobEffectCategory.BENEFICIAL, 0xbbbb88);
        this.addAttributeModifier(ErModAttributes.CRIT_RATE.get(), "9A32B354-DF69-6FF2-8EE7-3ACBB2F93C32", 0.14, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int amp, @NotNull AttributeModifier modifier) {
        return modifier.getAmount() + 0.035 * amp;
    }
}
