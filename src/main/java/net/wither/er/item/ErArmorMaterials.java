package net.wither.er.item;

import net.mcreator.er.init.ErModItems;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ErArmorMaterials implements ArmorMaterial {
    ANEMO(5, ElementalArmorItem.IRON_ARMOR, 15, SoundEvents.ARMOR_EQUIP_IRON,
            1F, 0.0F, () -> Ingredient.of(ErModItems.CONDENSED_ANEMO.get())),
    PYRO(5, ElementalArmorItem.IRON_ARMOR, 15, SoundEvents.ARMOR_EQUIP_IRON,
            1F, 0.0F, () -> Ingredient.of(ErModItems.CONDENSED_PYRO.get())),
    GEO(5, ElementalArmorItem.IRON_ARMOR, 15, SoundEvents.ARMOR_EQUIP_IRON,
            1F, 0.0F, () -> Ingredient.of(ErModItems.CONDENSED_GEO.get())),
    CRYO(5, ElementalArmorItem.IRON_ARMOR, 15, SoundEvents.ARMOR_EQUIP_IRON,
            1F, 0.0F, () -> Ingredient.of(ErModItems.CONDENSED_CRYO.get())),
    ELECTRO(5, ElementalArmorItem.IRON_ARMOR, 15, SoundEvents.ARMOR_EQUIP_IRON,
            1F, 0.0F, () -> Ingredient.of(ErModItems.CONDENSED_ELECTRO.get())),
    DENDRO(5, ElementalArmorItem.IRON_ARMOR, 15, SoundEvents.ARMOR_EQUIP_IRON,
            1F, 0.0F, () -> Ingredient.of(ErModItems.CONDENSED_DENDRO.get())),
    HYDRO(5, ElementalArmorItem.IRON_ARMOR, 15, SoundEvents.ARMOR_EQUIP_IRON,
            1F, 0.0F, () -> Ingredient.of(ErModItems.CONDENSED_HYDRO.get()));


    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;



    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 13);
        map.put(ArmorItem.Type.LEGGINGS, 15);
        map.put(ArmorItem.Type.CHESTPLATE, 16);
        map.put(ArmorItem.Type.HELMET, 11);
    });

    ErArmorMaterials(int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionFunctionForType, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionFunctionForType = protectionFunctionForType;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue(repairIngredient);
    }

    public int getDurabilityForType(ArmorItem.@NotNull Type type) {
        return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
    }

    public int getDefenseForType(ArmorItem.@NotNull Type type) {
        return this.protectionFunctionForType.get(type);
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public @NotNull SoundEvent getEquipSound() {
        return this.sound;
    }

    public @NotNull Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public @NotNull String getName() {
        return this.name().toLowerCase();
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
