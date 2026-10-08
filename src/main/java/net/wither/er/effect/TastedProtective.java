package net.wither.er.effect;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class TastedProtective extends MobEffect {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "food_effect.protective");
    public TastedProtective() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap map, int amp) {
        AttributeInstance armorInstance = map.getInstance(Attributes.ARMOR);
        if(armorInstance != null)
            armorInstance.addOrReplacePermanentModifier(new AttributeModifier(LOCATION, getArmorAmount(amp), AttributeModifier.Operation.ADD_VALUE));

        if(11 <= amp && amp <= 14){
            AttributeInstance shieldInstance = map.getInstance(ErModAttributes.SHIELD_STRENGTH);
            if(shieldInstance != null)
                shieldInstance.addOrReplacePermanentModifier(new AttributeModifier(LOCATION, getOtherAmount(amp, true), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }

        if(8 <= amp && amp <= 10){
            AttributeInstance healInstance = map.getInstance(ErModAttributes.INCOMING_HEALING_BONUS);
            if(healInstance != null)
                healInstance.addOrReplacePermanentModifier(new AttributeModifier(LOCATION, getOtherAmount(amp, false), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        AttributeInstance armorInstance = map.getInstance(Attributes.ARMOR);
        if(armorInstance != null)
            armorInstance.removeModifier(LOCATION);

        AttributeInstance shieldInstance = map.getInstance(ErModAttributes.SHIELD_STRENGTH);
        if(shieldInstance != null)
            shieldInstance.removeModifier(LOCATION);

        AttributeInstance healInstance = map.getInstance(ErModAttributes.INCOMING_HEALING_BONUS);
        if(healInstance != null)
            healInstance.removeModifier(LOCATION);
    }

    @Override
    public void createModifiers(int amp, @NotNull BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        consumer.accept(Attributes.ARMOR, new AttributeModifier(LOCATION, getArmorAmount(amp), AttributeModifier.Operation.ADD_VALUE));
        if(11 <= amp && amp <= 14)
            consumer.accept(ErModAttributes.SHIELD_STRENGTH, new AttributeModifier(LOCATION, getOtherAmount(amp, true), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        if(8 <= amp && amp <= 10)
            consumer.accept(ErModAttributes.INCOMING_HEALING_BONUS, new AttributeModifier(LOCATION, getOtherAmount(amp, false), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    public static double getArmorAmount(int amp) {
        return switch (amp){
            case 0 -> 88;
            case 1 -> 107;
            case 2 -> 126;
            case 3 -> 151;

            case 4, 11 -> 165;
            case 5, 12 -> 200;
            case 6, 13 -> 235;
            case 7, 14 -> 282;

            case 8 -> 215;
            case 9 -> 261;
            case 10 -> 308;
            default -> 20 * amp + 200;
        };
    }

    public static double getOtherAmount(int amp, boolean isShieldStrength) {
        if(isShieldStrength)
            return switch (amp){
                case 11 -> 0.25;
                case 12 -> 0.3;
                case 13 -> 0.35;
                case 14 -> 0.4;
                default -> 0;
            };
        return switch (amp){
            case 8 -> 0.06;
            case 9 -> 0.08;
            case 10 -> 0.1;
            default -> 0;
        };
    }
}
