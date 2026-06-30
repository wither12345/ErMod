package net.mcreator.er.init;

import net.wither.er.recipe.crafting.AlchemyCraftingRecipeCategory;
import net.wither.er.recipe.crafting.AlchemyCraftingRecipe;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;

import java.util.stream.Collectors;
import java.util.Objects;
import java.util.List;

@JeiPlugin
public class ErModJeiPlugin implements IModPlugin {
	public static mezz.jei.api.recipe.RecipeType<AlchemyCraftingRecipe> AlchemyCrafting_Type = new mezz.jei.api.recipe.RecipeType<>(AlchemyCraftingRecipeCategory.UID, AlchemyCraftingRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.parse("er:jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new AlchemyCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		List<AlchemyCraftingRecipe> AlchemyCraftingRecipes = recipeManager.getAllRecipesFor(AlchemyCraftingRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(AlchemyCrafting_Type, AlchemyCraftingRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ErModBlocks.CRAFTING_BENCH.get().asItem()), AlchemyCrafting_Type);
	}
}