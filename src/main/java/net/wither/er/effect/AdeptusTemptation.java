package net.wither.er.effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AdeptusTemptation extends MobEffect {
    private static final UUID ADEPTUS = UUID.fromString("26FB5933-DE05-0A2E-8ABF-CD8D77BCE160");
    public AdeptusTemptation() {
        super(MobEffectCategory.BENEFICIAL, 0xffffbb88);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "26FB5933-DE05-0A2E-8ABF-CD8D77BCE160", 37, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(ErModAttributes.CRIT_RATE.get(), "26FB5933-DE05-0A2E-8ABF-CD8D77BCE161", 0.09, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int amp, @NotNull AttributeModifier modifier) {
        if(modifier.getAmount() == 37)
            return 37 + amp * 8;
        return 0.09 + amp * 0.01;
    }
}
