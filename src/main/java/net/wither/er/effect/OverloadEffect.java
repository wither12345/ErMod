package net.wither.er.effect;

import net.mcreator.er.ErMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class OverloadEffect extends MobEffect {
    private static final ResourceLocation OVERLOAD = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "ability.overloaded");
    public OverloadEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap map, int level) {
        AttributeInstance attributeInstance = map.getInstance(Attributes.ATTACK_DAMAGE);
        if(attributeInstance != null)
            attributeInstance.addOrReplacePermanentModifier(new AttributeModifier(OVERLOAD, 0.2 + level * 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        AttributeInstance attributeInstance = map.getInstance(Attributes.ATTACK_DAMAGE);
        if(attributeInstance != null)
            attributeInstance.removeModifier(OVERLOAD);
    }
}
