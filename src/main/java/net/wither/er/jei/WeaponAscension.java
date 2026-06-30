package net.wither.er.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.mcreator.er.init.ErModBlocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.wither.er.item.data.WeaponLevelData;
import net.wither.er.recipe.ascension.AscensionRecipeListener;

import javax.annotation.Nullable;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;
import static net.mcreator.er.init.ErModItems.MORA_BAG;
import static net.wither.er.init.DataComponentsRegister.WEAPON_LEVEL;
import static net.wither.er.item.data.WeaponLevelData.create;
import static net.wither.er.recipe.ascension.AscensionRecipe.Input;

public class WeaponAscension implements IRecipeCategory<AscensionRecipeListener.AscensionEntry> {
    public final static ResourceLocation UID = new ResourceLocation("er:weapon_ascension");
    public final static ResourceLocation TEXTURE = new ResourceLocation("er:textures/screens/weapon_enhance_jei.png");
    private final IDrawable background;
    private final IDrawable icon;

    public WeaponAscension(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 160, 64);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ErModBlocks.ELEMENT_ANVIL.get().asItem()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<AscensionRecipeListener.AscensionEntry> getRecipeType() {
        return ErJeiPlugin.WeaponAscensionType;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Weapon Ascension");
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
    public void draw(AscensionRecipeListener.AscensionEntry recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AscensionRecipeListener.AscensionEntry recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder build = builder.addSlot(INPUT, 17, 21);
        ItemStack keyItem = new ItemStack(recipe.key());
        if (WEAPON_LEVEL.itemHas(keyItem)) {
            WeaponLevelData data = WEAPON_LEVEL.getData(keyItem);
            WEAPON_LEVEL.update(keyItem, t -> create(recipe.ascension(), true));
        }
        build.addItemStack(keyItem);

        build = builder.addSlot(INPUT, 71, 3);
        ItemStack moraBag = new ItemStack(MORA_BAG.get()).copy();
        moraBag.getOrCreateTag().putInt("moras", recipe.recipe().getMora());
        build.addItemStack(moraBag);

        for (int i = 0; i < 3; i++) {
            Input input = recipe.recipe().getInput(i);
            if (input != null)
                builder.addSlot(INPUT, 53 + 18 * i, 39).addItemStack(new ItemStack(input.getItem(), recipe.recipe().getInput(i).getCount()));
        }

        ItemStack resultItem = new ItemStack(recipe.key());
        if (WEAPON_LEVEL.itemHas(resultItem)) {
            WeaponLevelData data = WEAPON_LEVEL.getData(resultItem);
            WEAPON_LEVEL.update(resultItem, t -> create(recipe.ascension() + 1, false));
        }
        builder.addSlot(OUTPUT, 125, 21).addItemStack(resultItem);
    }

    @Override
    @Nullable
    public IDrawable getBackground() {
        return null;
    }
}
