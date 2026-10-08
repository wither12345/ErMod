package net.wither.er.effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class TastedPhysical extends MobEffect {
    public TastedPhysical() {
        super(MobEffectCategory.BENEFICIAL, 0);
        this.addAttributeModifier(ErModAttributes.PHYSICAL_DMG_BONUS.get(), "5DDDD126-76A5-A8AE-6C16-33032EE91F2F", 0.3, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amp, @NotNull AttributeModifier modifier) {
        return modifier.getAmount() + amp * 0.05;
    }
}
