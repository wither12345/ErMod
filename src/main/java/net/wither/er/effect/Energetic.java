package net.wither.er.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.init.ErAttributeRegister;
import org.jetbrains.annotations.NotNull;

public class Energetic extends MobEffect {
    public Energetic() {
        super(MobEffectCategory.BENEFICIAL, 0xdddd88);
        this.addAttributeModifier(ErAttributeRegister.STAMINA_COST.get(), "5DDDD126-76A5-A8AE-6C16-33032EE91F2F", -0.15, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amp, @NotNull AttributeModifier modifier) {
        return modifier.getAmount() - amp * 0.05;
    }
}
