package net.wither.er.init;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModPotions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.wither.er.elements.Element;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ElementalBrewingRecipes implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.parse("er:brewing_recipes");
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
            potion.set(DataComponents.POTION_CONTENTS, new PotionContents(ErModPotions.EMPTY_RESISTANCE_POTION));
            potion2.set(DataComponents.POTION_CONTENTS, new PotionContents(ElementalAttributesRegister.RES_POT.get(category)));
            brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
            ingredientStack.clear();

            ingredientStack.add(new ItemStack(category.getBrewIngredient()));
            potion.set(DataComponents.POTION_CONTENTS, new PotionContents(category.getDmgPotType() ? ErModPotions.EMPTY_DAMAGE_POTION_TYPE_2 : ErModPotions.EMPTY_DAMAGE_POTION_TYPE_1));
            potion2.set(DataComponents.POTION_CONTENTS, new PotionContents(ElementalAttributesRegister.DMG_POT.get(category)));
            brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
            ingredientStack.clear();
        }
        ingredientStack.add(new ItemStack((DeferredHolder<Item,Item>) ErModItems.LIZARD_TAIL));
        potion.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        potion2.set(DataComponents.POTION_CONTENTS, new PotionContents(ErModPotions.EMPTY_RESISTANCE_POTION));
        brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
        ingredientStack.clear();

        ingredientStack.add(new ItemStack((DeferredHolder<Item,Item>) ErModItems.FROG));
        potion.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        potion2.set(DataComponents.POTION_CONTENTS, new PotionContents(ErModPotions.EMPTY_DAMAGE_POTION_TYPE_1));
        brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
        ingredientStack.clear();

        ingredientStack.add(new ItemStack((DeferredHolder<Item,Item>) ErModItems.BUTTERFLY_WINGS));
        potion.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        potion2.set(DataComponents.POTION_CONTENTS, new PotionContents(ErModPotions.EMPTY_DAMAGE_POTION_TYPE_2));
        brewingRecipes.add(factory.createBrewingRecipe(List.copyOf(ingredientStack), potion.copy(), potion2.copy()));
        ingredientStack.clear();

        registration.addRecipes(RecipeTypes.BREWING, brewingRecipes);
    }
}