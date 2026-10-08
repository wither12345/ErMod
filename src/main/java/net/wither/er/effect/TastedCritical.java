package net.wither.er.effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class TastedCritical extends MobEffect {
    public TastedCritical() {
        super(MobEffectCategory.BENEFICIAL, 0);
        this.addAttributeModifier(ErModAttributes.CRIT_RATE.get(), "675BE365-C7FE-E262-A8B7-6FE16720C922", 1, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(ErModAttributes.CRIT_DAMAGE.get(), "675BE365-17FE-E262-A8B7-6FE16720C922", 0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int amp, @NotNull AttributeModifier modifier) {
        if(modifier.getAmount() == 0) return amp >= 6 ? 0.2 : 0;
        return switch (amp){
            case 0 -> 0.06;
            case 1 -> 0.09;
            case 2 -> 0.12;
            case 3 -> 0.15;
            case 4 -> 0.16;
            case 5 -> 0.18;
            case 6 -> 0.2;
            default -> 0.03 * amp + 0.03;
        };
    }
}
