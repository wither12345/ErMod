package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorMaterialsRegister {
    public static final DeferredRegister<ArmorMaterial> REGISTRY = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, ErMod.MODID);
    public static Holder<ArmorMaterial> ANEMO = REGISTRY.register(
            "anemo_armor", () -> createBasic(ErModItems.CONDENSED_ANEMO, "anemo_"));
    public static Holder<ArmorMaterial> PYRO = REGISTRY.register(
            "pyro_armor", () -> createBasic(ErModItems.CONDENSED_PYRO, "pyro_"));
    public static Holder<ArmorMaterial> GEO = REGISTRY.register(
            "geo_armor", () -> createBasic(ErModItems.CONDENSED_GEO, "geo_"));
    public static Holder<ArmorMaterial> ELECTRO = REGISTRY.register(
            "electro_armor", () -> createBasic(ErModItems.CONDENSED_ELECTRO, "electro_"));
    public static Holder<ArmorMaterial> DENDRO = REGISTRY.register(
            "dendro_armor", () -> createBasic(ErModItems.CONDENSED_DENDRO, "dendro_"));
    public static Holder<ArmorMaterial> CRYO = REGISTRY.register(
            "cryo_armor", () -> createBasic(ErModItems.CONDENSED_CRYO, "cryo_"));
    public static Holder<ArmorMaterial> HYDRO = REGISTRY.register(
            "hydro_armor", () -> createBasic(ErModItems.CONDENSED_HYDRO, "hydro_"));

    private static ArmorMaterial createBasic(Supplier<Item> repairIngredient, String name){
        return new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 2);
            map.put(ArmorItem.Type.LEGGINGS, 5);
            map.put(ArmorItem.Type.CHESTPLATE, 6);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 6);
        }), 9, SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(repairIngredient::get), List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, name))), 0.1f, 0f);
    }
}
