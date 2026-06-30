package net.wither.er.effect;

import net.mcreator.er.ErMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.elements.Element;

public class ElementalRESEffect extends MobEffect {
    public ElementalRESEffect(Element.Category category) {
        super(MobEffectCategory.BENEFICIAL, category.getColor());
        this.addAttributeModifier(category.getResAttr(), ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "effect.res"), 25, AttributeModifier.Operation.ADD_VALUE);
    }
}
