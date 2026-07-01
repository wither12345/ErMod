package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.mcreator.er.jei_recipes.AlchemyCraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypeRegister {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ErMod.MODID);

    public static final RegistryObject<RecipeType<AlchemyCraftingRecipe>> ALCHEMY_CRAFT_TYPE =
            RECIPE_TYPES.register("alchemy_crafting",
                    () -> AlchemyCraftingRecipe.Type.INSTANCE);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ErMod.MODID);

    public static final RegistryObject<RecipeSerializer<AlchemyCraftingRecipe>> ALCHEMY_CRAFT_SERIALIZER =
            RECIPE_SERIALIZERS.register("alchemy_crafting", () -> AlchemyCraftingRecipe.Serializer.INSTANCE);
}