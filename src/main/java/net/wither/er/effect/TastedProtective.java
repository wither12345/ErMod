package net.wither.er.effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class TastedProtective extends MobEffect {
    public TastedProtective() {
        super(MobEffectCategory.BENEFICIAL, 0);
        this.addAttributeModifier(Attributes.ARMOR, "1DDDD126-76A5-A8AE-6C16-33032EE91F2F", 1, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(ErModAttributes.SHIELD_STRENGTH.get(), "0DDDD126-76A5-A8AE-6C16-33032EE91F2F", 0, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(ErModAttributes.INCOMING_HEALING_BONUS.get(), "2DDDD126-76A5-A8AE-6C16-33032EE91F2F", 2, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amp, @NotNull AttributeModifier modifier) {
        if(modifier.getAmount() == 0)
            return switch (amp){
                case 11 -> 0.25;
                case 12 -> 0.3;
                case 13 -> 0.35;
                case 14 -> 0.4;
                default -> 0;
            };
        if(modifier.getAmount() == 2)
            return switch (amp){
                case 8 -> 0.06;
                case 9 -> 0.08;
                case 10 -> 0.1;
                default -> 0;
            };
        return switch (amp){
            case 0 -> 88;
            case 1 -> 107;
            case 2 -> 126;
            case 3 -> 151;

            case 4, 11 -> 165;
            case 5, 12 -> 200;
            case 6, 13 -> 235;
            case 7, 14 -> 282;

            case 8 -> 215;
            case 9 -> 261;
            case 10 -> 308;
            default -> 20 * amp + 200;
        };
    }
}
