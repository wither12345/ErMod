package net.wither.er.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Overloaded extends MobEffect{
    private static final UUID OVERLOAD = UUID.fromString("4727E158-44DD-7161-28F3-338EC6D7E844");
    public Overloaded() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity living, @NotNull AttributeMap map, int level) {
        AttributeInstance attributeInstance = map.getInstance(Attributes.ATTACK_DAMAGE);
        if(attributeInstance != null) {
            attributeInstance.removeModifier(OVERLOAD);
            attributeInstance.addPermanentModifier(new AttributeModifier(OVERLOAD, "overloaded", 0.2 + level * 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity living, AttributeMap map, int level) {
        AttributeInstance attributeInstance = map.getInstance(Attributes.ATTACK_DAMAGE);
        if(attributeInstance != null)
            attributeInstance.removeModifier(OVERLOAD);
    }
}
