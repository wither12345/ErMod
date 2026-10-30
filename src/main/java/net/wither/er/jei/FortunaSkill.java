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
import net.mcreator.er.init.ErModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.recipe.ascension.AscensionRecipe;
import net.wither.er.recipe.ascension.AscensionRecipeListener;
import org.jetbrains.annotations.NotNull;

public class FortunaSkill implements IRecipeCategory<AscensionRecipeListener.AscensionEntry> {
    public final static ResourceLocation UID = ResourceLocation.parse("er:fortuna_skill");
    public final static ResourceLocation TEXTURE = ResourceLocation.parse("er:textures/screens/fortuna_enhance_jei.png");
    private final IDrawable background;
    private final IDrawable icon;

    public FortunaSkill(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 100, 64);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ErModItems.FORTUNA_ENHANCER.toStack());
    }

    @Override
    public mezz.jei.api.recipe.@NotNull RecipeType<AscensionRecipeListener.AscensionEntry> getRecipeType() {
        return ErJeiPlugin.FORTUNA_SKILL_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("er.lore.jei.fortuna_skill");
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
    public void draw(AscensionRecipeListener.@NotNull AscensionEntry recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AscensionRecipeListener.AscensionEntry recipe, @NotNull IFocusGroup focuses) {
        IRecipeSlotBuilder build = builder.addSlot(RecipeIngredientRole.INPUT, 40, 22);
        ItemStack keyItem = new ItemStack(recipe.key()) ;

        build.addItemStack(keyItem) ;

        build = builder.addSlot(RecipeIngredientRole.INPUT, 40, 4);
        ItemStack moraBag = new ItemStack(ErModItems.MORA_BAG.get()).copy();
        moraBag.set(DataComponentsRegister.MORA_BAG.get(), recipe.recipe().getMora());
        build.addItemStack(moraBag) ;

        for(int i = 0 ; i < 5 ; i ++) {
            AscensionRecipe.Input input = recipe.recipe().getInput(i);
            if (input != null)
                builder.addSlot(RecipeIngredientRole.INPUT, 4 + 18 * i, 40).addItemStack(new ItemStack(input.getItem(), recipe.recipe().getInput(i).getCount()));
        }
    }
}
