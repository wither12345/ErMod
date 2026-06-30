package net.wither.er.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.mcreator.er.init.ErModBlocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.wither.er.recipe.converting.AlchemyConvertingRecipe;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class AlchemyConverting implements IRecipeCategory<AlchemyConvertingRecipe> {
    public final static ResourceLocation UID = new ResourceLocation("er:alchemy_converting");
    public final static ResourceLocation TEXTURE = new ResourceLocation("er:textures/screens/alchemy_converting_jei.png");
    private final IDrawable background;
    private final IDrawable icon;

    public AlchemyConverting(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 128, 64);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ErModBlocks.CRAFTING_BENCH.get().asItem()));
    }

    @Override
    public mezz.jei.api.recipe.@NotNull RecipeType<AlchemyConvertingRecipe> getRecipeType() {
        return ErJeiPlugin.AlchemyConvertingType;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Alchemy Converting");
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return this.background.getWidth();
    }

    @Override
    public int getHeight() {
        return this.background.getHeight();
    }

    @Override
    public void draw(@NotNull AlchemyConvertingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlchemyConvertingRecipe recipe, @NotNull IFocusGroup focuses) {
        IRecipeSlotBuilder build = builder.addSlot(RecipeIngredientRole.INPUT, 10, 24);
        build.addItemStacks(recipe.results());

        build = builder.addSlot(RecipeIngredientRole.INPUT, 55, 24);
        build.addItemStack(new ItemStack(recipe.mainMaterial(), recipe.count()));

        build = builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 24);
        build.addItemStacks(recipe.results());
    }

    @Override
    @Nullable
    public IDrawable getBackground() {
        return null;
    }
}
