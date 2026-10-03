package net.wither.er.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.ForgeRegistries;
import net.wither.er.network.ErCombatVariables;

import java.util.ArrayList;
import java.util.List;

public record ErFoodData(List<MobEffectInstance> applyEffects, float healAmount, float healPercent, float staminaRecover) implements FoodDataProvider{
    public static final Codec<ErFoodData> CODEC;
    public static final Codec<MobEffectInstance> INSTANCE_CODEC;

    static {
        INSTANCE_CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        ForgeRegistries.MOB_EFFECTS.getCodec().fieldOf("id").forGetter(MobEffectInstance::getEffect),
                        Codec.INT.optionalFieldOf("duration", 0).forGetter(MobEffectInstance::getDuration),
                        Codec.INT.optionalFieldOf("amplifier", 0).forGetter(MobEffectInstance::getAmplifier)
                ).apply(instance, MobEffectInstance::new)
        );
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        INSTANCE_CODEC.listOf().optionalFieldOf("apply_effects", List.of()).forGetter(ErFoodData::applyEffects),
                        Codec.FLOAT.optionalFieldOf("heal_amount", 0f).forGetter(ErFoodData::healAmount),
                        Codec.FLOAT.optionalFieldOf("heal_percent", 0f).forGetter(ErFoodData::healPercent),
                        Codec.FLOAT.optionalFieldOf("stamina_recover", 0f).forGetter(ErFoodData::staminaRecover)
                ).apply(instance, ErFoodData::new));
    }

    public void applyTo(LivingEntity living){
        applyEffects.forEach(
                instance -> living.addEffect(new MobEffectInstance(instance))
        );
        living.heal(this.healAmount + living.getMaxHealth() * this.healPercent);
        ErCombatVariables.PlayerVariables var = living.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElse(new ErCombatVariables.PlayerVariables());
        var.stamina += staminaRecover;

        if(staminaRecover > 0) {
            AttributeInstance instance = living.getAttribute(ErModAttributes.MAX_STAMINA.get());
            if (instance != null && var.stamina > instance.getValue())
                var.stamina = instance.getValue();
            var.syncWithId(living, 0b100);
        }
    }

    public void addTooltip(List<Component> components){
        String heal = "";
        int index = 1;
        if(this.healAmount > 0)
            heal += this.healAmount;
        if(this.healPercent > 0)
            heal += (heal.isEmpty() ? "" : " + ") + healPercent * 100 + "%";
        if(!heal.isEmpty())
            components.add(index ++, Component.translatable("lore.er.food.heal").append(Component.literal(heal).setStyle(Style.EMPTY.withColor(0x00ff00))));
        if(!this.applyEffects.isEmpty()) {
            List<Component> pot = new ArrayList<>();
            PotionUtils.addPotionTooltip(this.applyEffects, pot, 1);
            components.addAll(index ++, pot);
        }
    }

    @Override
    public ErFoodData getFoodData(ItemStack itemStack) {
        return this;
    }
}
