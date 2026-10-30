package net.wither.er.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
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
    public static final RecipeType<AscensionRecipeListener.AscensionEntry> WEAPON_ASCENSION_TYPE = new RecipeType<>(WeaponAscension.UID, AscensionRecipeListener.AscensionEntry.class);
    public static final RecipeType<AscensionRecipeListener.AscensionEntry> FORTUNA_ASCENSION_TYPE = new RecipeType<>(FortunaAscension.UID, AscensionRecipeListener.AscensionEntry.class);
    public static final RecipeType<AscensionRecipeListener.AscensionEntry> FORTUNA_SKILL_TYPE = new RecipeType<>(FortunaSkill.UID, AscensionRecipeListener.AscensionEntry.class);
    public static final RecipeType<AlchemyConvertingRecipe> ALCHEMY_CONVERTING_TYPE = new RecipeType<>(AlchemyConverting.UID, AlchemyConvertingRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.parse("er:jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WeaponAscension(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FortunaAscension(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FortunaSkill(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AlchemyConverting(registration.getJeiHelpers().getGuiHelper()));
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

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.ELEMENT_ANVIL.get().asItem()), WEAPON_ASCENSION_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.FORTUNA_ENHANCER.get().asItem()), FORTUNA_ASCENSION_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.FORTUNA_ENHANCER.get().asItem()), FORTUNA_SKILL_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ErModBlocks.CRAFTING_BENCH.get().asItem()), ALCHEMY_CONVERTING_TYPE);
    }
}
