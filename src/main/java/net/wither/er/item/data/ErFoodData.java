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
import net.minecraft.world.item.alchemy.PotionContents;
import net.wither.er.network.ErCombatVariables;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
        applyEffects.forEach(instance -> living.addEffect(new MobEffectInstance(instance)));
        living.heal(this.healAmount + living.getMaxHealth() * this.healPercent);

        if(staminaRecover > 0) {
            ErCombatVariables.PlayerVariables var = living.getData(ErCombatVariables.PLAYER_VARIABLES);
            var.stamina += staminaRecover;
            var.staminaRecoveryCooldown = 0;

            AttributeInstance instance = living.getAttribute(ErModAttributes.MAX_STAMINA);
            if (instance != null && var.stamina > instance.getValue())
                var.stamina = instance.getValue();
            var.syncWithId(living, 0b1100);
        }
    }

    public void addTooltip(List<Component> components){
        String heal = "";
        int index = 1;
        if(this.healAmount > 0)
            heal += new DecimalFormat("##").format(this.healAmount);
        if(this.healPercent > 0)
            heal += (heal.isEmpty() ? "" : " + ") + new DecimalFormat("##.#%").format(healPercent);
        if(!heal.isEmpty())
            components.add(index ++, Component.translatable("lore.er.food.heal").append(Component.literal(heal).setStyle(Style.EMPTY.withColor(0x00ff00))));
        if(!this.applyEffects.isEmpty()) {
            ArrayList<Component> pot = new ArrayList<>();
            PotionContents.addPotionTooltip(this.applyEffects, pot::add, 0.05f, 1);
            components.addAll(index ++, pot);
        }
        if(this.staminaRecover > 0)
            components.add(index ++, Component.translatable("lore.er.food.stamina").append(Component.literal(String.valueOf(staminaRecover)).setStyle(Style.EMPTY.withColor(0xffff00))));
    }

    @Override
    public ErFoodData getFoodData(ItemStack itemStack) {
        return this;
    }

    @Override
    public boolean replacePotion() {
        return true;
    }
}
