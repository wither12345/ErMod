package net.wither.er.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.elements.Element;

public class ElementalRESEffect extends MobEffect {
    public ElementalRESEffect(Element.Category category) {
        super(MobEffectCategory.BENEFICIAL, category.getColor());
        this.addAttributeModifier(category.getResAttr(), "174280A1-28AF-CC14-6E84-FCD4AA65AD84", 25, AttributeModifier.Operation.ADDITION);
    }
}
