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
import net.wither.er.init.ErAttributeRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class Energetic extends MobEffect {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "food_effect.energetic");
    public Energetic() {
        super(MobEffectCategory.BENEFICIAL, 0xdddd88);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap map, int level) {
        AttributeInstance attributeInstance = map.getInstance(ErAttributeRegister.STAMINA_COST);
        if(attributeInstance != null)
            attributeInstance.addOrReplacePermanentModifier(new AttributeModifier(LOCATION, -0.15 - 0.05 * level, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        AttributeInstance attributeInstance = map.getInstance(ErAttributeRegister.STAMINA_COST);
        if(attributeInstance != null)
            attributeInstance.removeModifier(LOCATION);
    }

    @Override
    public void createModifiers(int amp, @NotNull BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        consumer.accept(ErAttributeRegister.STAMINA_COST, new AttributeModifier(LOCATION, -0.15 - 0.05 * amp, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }
}
