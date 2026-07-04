package net.wither.er.init;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModPotions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ElementalBrewingRecipes implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation("er:brewing_recipes");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
        List<IJeiBrewingRecipe> brewingRecipes = new ArrayList<>();
        ItemStack potion = new ItemStack(Items.POTION);
        ItemStack potion2 = new ItemStack(Items.POTION);
        List<ItemStack> ingredientStack = new ArrayList<>();
        List<ItemStack> inputStack = new ArrayList<>();
        for(Element.Category category : Element.Category.values()) {
            ingredientStack.add(new ItemStack(category.getBrewIngredient()));
            PotionUtils.setPotion(potion, ErModPotions.EMPTY_RESISTANCE_POTION.get());
            PotionUtils.setPotion(potion2, ElementalAttributesRegister.RES_POT.get(category).get());
            brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
            ingredientStack.clear();

            ingredientStack.add(new ItemStack(category.getBrewIngredient()));
            PotionUtils.setPotion(potion, category.getDmgPotType() ? ErModPotions.EMPTY_DAMAGE_POTION_TYPE_2.get() : ErModPotions.EMPTY_DAMAGE_POTION_TYPE_1.get());
            PotionUtils.setPotion(potion2, ElementalAttributesRegister.DMG_POT.get(category).get());
            brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
            ingredientStack.clear();
        }
        ingredientStack.add(new ItemStack(ErModItems.LIZARD_TAIL.get()));
        PotionUtils.setPotion(potion, Potions.WATER);
        PotionUtils.setPotion(potion2, ErModPotions.EMPTY_RESISTANCE_POTION.get());
        brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
        ingredientStack.clear();

        ingredientStack.add(new ItemStack(ErModItems.FROG.get()));
        PotionUtils.setPotion(potion, Potions.WATER);
        PotionUtils.setPotion(potion2, ErModPotions.EMPTY_DAMAGE_POTION_TYPE_1.get());
        brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
        ingredientStack.clear();

        ingredientStack.add(new ItemStack(ErModItems.BUTTERFLY_WINGS.get()));
        PotionUtils.setPotion(potion, Potions.WATER);
        PotionUtils.setPotion(potion2, ErModPotions.EMPTY_DAMAGE_POTION_TYPE_2.get());
        brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
        ingredientStack.clear();

        registration.addRecipes(RecipeTypes.BREWING, brewingRecipes);
    }
}