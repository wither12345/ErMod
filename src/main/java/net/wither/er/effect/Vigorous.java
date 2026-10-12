package net.wither.er.effect;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class Vigorous extends MobEffect {
    private static final ResourceLocation VIGOROUS = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "ability.vigorous");
    public Vigorous() {
        super(MobEffectCategory.BENEFICIAL, 0xbbbb88);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap map, int level) {
        AttributeInstance attributeInstance = map.getInstance(ErModAttributes.CRIT_RATE);
        if(attributeInstance != null)
            attributeInstance.addOrReplacePermanentModifier(new AttributeModifier(VIGOROUS, 0.035 * level + 0.14, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        AttributeInstance attributeInstance = map.getInstance(ErModAttributes.CRIT_RATE);
        if(attributeInstance != null)
            attributeInstance.removeModifier(VIGOROUS);
    }

}
