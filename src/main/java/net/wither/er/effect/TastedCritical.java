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

public class TastedCritical extends MobEffect {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "food_effect.critical");
    public TastedCritical() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap map, int amp) {
        AttributeInstance rateInstance = map.getInstance(ErModAttributes.CRIT_RATE);
        if(rateInstance != null)
            rateInstance.addOrReplacePermanentModifier(new AttributeModifier(LOCATION, getRateAmount(amp), AttributeModifier.Operation.ADD_VALUE));

        AttributeInstance dmgInstance = map.getInstance(ErModAttributes.CRIT_DAMAGE);
        if(dmgInstance != null && amp >= 6)
            dmgInstance.addOrReplacePermanentModifier(new AttributeModifier(LOCATION, 0.2, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        AttributeInstance rateInstance = map.getInstance(ErModAttributes.CRIT_RATE);
        if(rateInstance != null)
            rateInstance.removeModifier(LOCATION);

        AttributeInstance dmgInstance = map.getInstance(ErModAttributes.CRIT_DAMAGE);
        if(dmgInstance != null)
            dmgInstance.removeModifier(LOCATION);
    }

    @Override
    public void createModifiers(int amp, @NotNull BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        consumer.accept(ErModAttributes.CRIT_RATE, new AttributeModifier(LOCATION, getRateAmount(amp), AttributeModifier.Operation.ADD_VALUE));
        if(amp >= 6)
            consumer.accept(ErModAttributes.CRIT_DAMAGE, new AttributeModifier(LOCATION, 0.2, AttributeModifier.Operation.ADD_VALUE));
    }

    public static double getRateAmount(int amp) {
        return switch (amp){
            case 0 -> 0.06;
            case 1 -> 0.09;
            case 2 -> 0.12;
            case 3 -> 0.15;
            case 4 -> 0.16;
            case 5 -> 0.18;
            case 6 -> 0.2;
            default -> 0.03 * amp + 0.03;
        };
    }
}
