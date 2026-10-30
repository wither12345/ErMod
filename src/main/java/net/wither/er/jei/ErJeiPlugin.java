package net.wither.er.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.mcreator.er.init.ErModBlocks;
import net.mcreator.er.jei_recipes.AlchemyCraftingRecipe;
import net.mcreator.er.jei_recipes.AlchemyCraftingRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.wither.er.recipe.ascension.AscensionRecipeListener;
import net.wither.er.recipe.converting.AlchemyConvertingRecipe;
import net.wither.er.recipe.converting.AlchemyConvertingRecipeListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@JeiPlugin
public class ErJeiPlugin implements IModPlugin {
    public static RecipeType<AscensionRecipeListener.AscensionEntry> WEAPON_ASCENSION_TYPE = new RecipeType<>(WeaponAscension.UID, AscensionRecipeListener.AscensionEntry.class);
    public static RecipeType<AscensionRecipeListener.AscensionEntry> FORTUNA_ASCENSION_TYPE = new RecipeType<>(FortunaAscension.UID, AscensionRecipeListener.AscensionEntry.class);
    public static RecipeType<AscensionRecipeListener.AscensionEntry> FORTUNA_SKILL_TYPE = new RecipeType<>(FortunaSkill.UID, AscensionRecipeListener.AscensionEntry.class);
    public static RecipeType<AlchemyConvertingRecipe> ALCHEMY_CONVERTING_TYPE = new RecipeType<>(AlchemyConverting.UID, AlchemyConvertingRecipe.class);
    public static RecipeType<AlchemyCraftingRecipe> ALCHEMY_CRAFTING_TYPE = new RecipeType<>(AlchemyCraftingRecipeCategory.UID, AlchemyCraftingRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation("er:jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WeaponAscension(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AlchemyConverting(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FortunaAscension(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FortunaSkill(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AlchemyCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<AscensionRecipeListener.AscensionEntry> weaponAscensionList = AscensionRecipeListener.getWeaponAscensionList() ;
        registration.addRecipes(WEAPON_ASCENSION_TYPE, weaponAscensionList);

        List<AscensionRecipeListener.AscensionEntry> fortunaAscensionList = AscensionRecipeListener.getFortunaAscensionList() ;
        registration.addRecipes(FORTUNA_ASCENSION_TYPE, fortunaAscensionList);

        List<AscensionRecipeListener.AscensionEntry> fortunaSkillList = AscensionRecipeListener.getFortunaSkillList() ;
        registration.addRecipes(FORTUNA_SKILL_TYPE, fortunaSkillList);


        List<AlchemyConvertingRecipe> alchemyConvertingRecipes = AlchemyConvertingRecipeListener.getRecipeList();
        registration.addRecipes(ALCHEMY_CONVERTING_TYPE, alchemyConvertingRecipes);
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<AlchemyCraftingRecipe> AlchemyCraftingRecipes = new ArrayList<>(recipeManager.getAllRecipesFor(AlchemyCraftingRecipe.Type.INSTANCE));
        registration.addRecipes(ALCHEMY_CRAFTING_TYPE, AlchemyCraftingRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.ELEMENT_ANVIL.get().asItem()), WEAPON_ASCENSION_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.FORTUNA_ENHANCER.get().asItem()), FORTUNA_SKILL_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.FORTUNA_ENHANCER.get().asItem()), FORTUNA_ASCENSION_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.CRAFTING_BENCH.get().asItem()), ALCHEMY_CONVERTING_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.CRAFTING_BENCH.get().asItem()), ALCHEMY_CRAFTING_TYPE);
    }
}