package net.wither.er.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.elements.Element;

public class ElementalDMGEffect extends MobEffect {
    public ElementalDMGEffect(Element.Category category) {
        super(MobEffectCategory.BENEFICIAL, category.getColor());
        this.addAttributeModifier(category.getDamageAttr(), "5AA1D102-EE54-0CBA-A942-05CBFD80DE5F", 0.25, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}
