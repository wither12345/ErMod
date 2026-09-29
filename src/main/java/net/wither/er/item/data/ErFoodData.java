package net.wither.er.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.wither.er.network.ErCombatVariables;

import java.util.List;

public record ErFoodData(List<MobEffectInstance> applyEffects, float healAmount, float healPercent, float staminaRecover) implements FoodDataProvider{
    public static final Codec<ErFoodData> CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        MobEffectInstance.CODEC.listOf().optionalFieldOf("apply_effects", List.of()).forGetter(ErFoodData::applyEffects),
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
        ErCombatVariables.PlayerVariables var = living.getData(ErCombatVariables.PLAYER_VARIABLES);
        var.stamina += staminaRecover;

        if(staminaRecover > 0) {
            AttributeInstance instance = living.getAttribute(ErModAttributes.MAX_STAMINA);
            if (instance != null && var.stamina > instance.getValue())
                var.stamina = instance.getValue();
            var.syncWithId(living, 0b100);
        }
    }

    @Override
    public ErFoodData getFoodData(ItemStack itemStack) {
        return this;
    }
}
