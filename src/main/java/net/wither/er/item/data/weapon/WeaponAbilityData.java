package net.wither.er.item.data.weapon;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.init.WeaponAbilityRegister;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.function.Supplier;

public record WeaponAbilityData(Supplier<Object> ability, Supplier<Item> ascension, Attribute attribute, double baseAmount, boolean type) {
    public static final ResourceLocation LOCATION = new ResourceLocation("er", "weapon_ability");
    public static final Capability<WeaponAbilityData> WEAPON_ABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public AttributeModifier getModifier(UUID uuid, int level){
        double multi = 1 + (level / 5) * 0.2d ;
        return new AttributeModifier(
                uuid, "secondary", this.getFinalAmount(baseAmount * multi), this.type ? AttributeModifier.Operation.MULTIPLY_BASE : AttributeModifier.Operation.ADDITION
        );
    }

    private double getFinalAmount(double amount){
        if(type) {
            BigDecimal bd = new BigDecimal(amount);
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
        return (int)(amount + 0.5);
    }

    private static class Builder{
        public Supplier<Object> ability;
        public Supplier<Item> ascension;
        public Attribute attribute;
        public double baseAmount;
        public boolean type;

        public Builder(WeaponAbilityData data){
            this.ability = data.ability();
            this.ascension = data.ascension();
            this.attribute = data.attribute();
            this.baseAmount = data.baseAmount();
            this.type = data.type();
        }

        public WeaponAbilityData build(){
            return new WeaponAbilityData(ability, ascension, attribute, baseAmount, type);
        }
    }

    public static class CapabilityProvider implements ICapabilitySerializable<CompoundTag> {
        private WeaponAbilityData data ;
        private LazyOptional<WeaponAbilityData> instance  ;

        public CapabilityProvider(WeaponAbilityData data){
            this.data = data;
            instance = LazyOptional.of(() -> data);
        }

        public CapabilityProvider(RegistryObject<Object> ability){
            this(new WeaponAbilityData(ability, Items.AIR.builtInRegistryHolder(), Attributes.MOVEMENT_SPEED, 0, false));
        }

        public CapabilityProvider(Attribute attribute, double baseAmount, boolean type){
            this(new WeaponAbilityData(WeaponAbilityRegister.EMPTY, Items.AIR.builtInRegistryHolder(), attribute, baseAmount, type));
        }

        public CapabilityProvider(RegistryObject<Object> ability, RegistryObject<Item> ascension, Attribute attribute, double baseAmount, boolean type){
            this(new WeaponAbilityData(ability, ascension, attribute, baseAmount, type));
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return cap == WEAPON_ABILITY ? instance.cast() : LazyOptional.empty();
        }

        //@Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString("ability", WeaponAbilityRegister.ABILITY_REGISTRY.getKey(data.ability().get()).toString());
            tag.putString("ascension", ForgeRegistries.ITEMS.getKey(data.ascension().get()).toString());
            tag.putString("attribute", ForgeRegistries.ATTRIBUTES.getKey(data.attribute()).toString());
            tag.putDouble("baseAmount", this.data.baseAmount());
            tag.putBoolean("type", this.data.type());
            return tag;
        }

        //@Override
        public void deserializeNBT(CompoundTag tag) {
            Builder builder = new Builder(this.data);
            if(tag.contains("ability")) {
                Object ability = WeaponAbilityRegister.ABILITY_REGISTRY.getValue(new ResourceLocation(tag.getString("ability")));
                if(ability != null)
                    builder.ability = () -> ability;
            }
            if(tag.contains("ascension")) {
                ForgeRegistries.ITEMS.getHolder(new ResourceLocation(tag.getString("ascension")))
                        .ifPresent(ascension -> builder.ascension = ascension);
            }
            if(tag.contains("attribute")) {
                Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(tag.getString("attribute")));
                if(attr != null)
                    builder.attribute = attr;
            }
            if(tag.contains("baseAmount")) {
                builder.baseAmount = tag.getDouble("baseAmount");
            }
            if(tag.contains("type")) {
                builder.type = tag.getBoolean("type");
            }
            this.data = builder.build();
            instance = LazyOptional.of(builder::build);
        }
    }
}
