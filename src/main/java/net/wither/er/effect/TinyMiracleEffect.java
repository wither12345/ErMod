package net.wither.er.effect;

import net.mcreator.er.ErMod;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

public class TinyMiracleEffect extends MobEffect {
    private static final AttributeModifier RES_MODIFIER =
        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "tiny_miracle.hurt"), 30, AttributeModifier.Operation.ADD_VALUE);

    public TinyMiracleEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x555aa0);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap map, int i) {
        if(i < 7) {
            Holder<Attribute> attr = Element.Category.values()[i].getResAttr();
            AttributeInstance instance = map.getInstance(attr);
            if(instance != null)
                instance.addPermanentModifier(RES_MODIFIER);
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull AttributeMap map) {
        for(Element.Category category : Element.Category.values()){
            AttributeInstance instance = map.getInstance(category.getResAttr());
            if(instance != null)
                instance.removeModifier(RES_MODIFIER);
        }
    }
}
