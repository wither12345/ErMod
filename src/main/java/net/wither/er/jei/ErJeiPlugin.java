package net.wither.er.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.mcreator.er.init.ErModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.wither.er.recipe.ascension.AscensionRecipeListener;
import net.wither.er.recipe.converting.AlchemyConvertingRecipe;
import net.wither.er.recipe.converting.AlchemyConvertingRecipeListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class ErJeiPlugin implements IModPlugin {
    public static mezz.jei.api.recipe.RecipeType<AscensionRecipeListener.AscensionEntry> WeaponAscensionType = new mezz.jei.api.recipe.RecipeType<>(WeaponAscension.UID, AscensionRecipeListener.AscensionEntry.class);
    public static mezz.jei.api.recipe.RecipeType<AlchemyConvertingRecipe> AlchemyConvertingType = new mezz.jei.api.recipe.RecipeType<>(AlchemyConverting.UID, AlchemyConvertingRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.parse("er:jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WeaponAscension(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AlchemyConverting(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<AscensionRecipeListener.AscensionEntry> AscensionRecipes = AscensionRecipeListener.getAscensionEntryList() ;
        registration.addRecipes(WeaponAscensionType, AscensionRecipes);

        List<AlchemyConvertingRecipe> alchemyConvertingRecipes = AlchemyConvertingRecipeListener.getRecipeList();
        registration.addRecipes(AlchemyConvertingType, alchemyConvertingRecipes);

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.ELEMENT_ANVIL.get().asItem()), WeaponAscensionType);
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.CRAFTING_BENCH.get().asItem()), AlchemyConvertingType);
    }
}
