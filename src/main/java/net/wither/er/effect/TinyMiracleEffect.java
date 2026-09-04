package net.wither.er.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TinyMiracleEffect extends MobEffect {
    private static final AttributeModifier RES_MODIFIER =
        new AttributeModifier(UUID.fromString("1BA78DFA-C5E0-6D30-FADD-2B6A4ED2A18C"), "tiny_miracle_hurt", 30, AttributeModifier.Operation.ADDITION);

    public TinyMiracleEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x555aa0);
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity living, @NotNull AttributeMap map, int i) {
        if(i < 7) {
            Attribute attr = Element.Category.values()[i].getResAttr();
            AttributeInstance instance = map.getInstance(attr);
            if(instance != null)
                instance.addPermanentModifier(RES_MODIFIER);
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity living, @NotNull AttributeMap map, int i) {
        for(Element.Category category : Element.Category.values()){
            AttributeInstance instance = map.getInstance(category.getResAttr());
            if(instance != null)
                instance.removeModifier(RES_MODIFIER);
        }
    }
}
