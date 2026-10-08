package net.wither.er.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class TastedStrong extends MobEffect {
    public TastedStrong() {
        super(MobEffectCategory.BENEFICIAL, 0);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "DD1E65D6-D6BA-4C81-FC99-31195F44D3E8", 4, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int amp, @NotNull AttributeModifier modifier) {
        return modifier.getAmount() + amp * 6;
    }
}
