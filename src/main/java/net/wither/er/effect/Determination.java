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

public class Determination extends MobEffect {
    private static final ResourceLocation DETERMINATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "ability.determination");
    public Determination() {
        super(MobEffectCategory.BENEFICIAL, 0xbbffbb);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap map, int level) {
        AttributeInstance attributeInstance = map.getInstance(Attributes.ATTACK_DAMAGE);
        if(attributeInstance != null)
            attributeInstance.addOrReplacePermanentModifier(new AttributeModifier(DETERMINATION, 0.12 + level * 0.03, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        AttributeInstance attributeInstance = map.getInstance(Attributes.ATTACK_DAMAGE);
        if(attributeInstance != null)
            attributeInstance.removeModifier(DETERMINATION);
    }
}
