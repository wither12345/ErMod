package net.wither.er.effect;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class TastedPhysical extends MobEffect {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "food_effect.physical");
    public TastedPhysical() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap map, int level) {
        AttributeInstance attributeInstance = map.getInstance(ErModAttributes.PHYSICAL_DMG_BONUS);
        if(attributeInstance != null)
            attributeInstance.addOrReplacePermanentModifier(new AttributeModifier(LOCATION, 0.3 + level * 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        AttributeInstance attributeInstance = map.getInstance(ErModAttributes.PHYSICAL_DMG_BONUS);
        if(attributeInstance != null)
            attributeInstance.removeModifier(LOCATION);
    }

    @Override
    public void createModifiers(int amp, @NotNull BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        consumer.accept(ErModAttributes.PHYSICAL_DMG_BONUS, new AttributeModifier(LOCATION, 0.3 + amp * 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }
}
