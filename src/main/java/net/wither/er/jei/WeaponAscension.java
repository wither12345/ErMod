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
import net.mcreator.er.init.ErModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.weapon.WeaponLevelData;
import net.wither.er.recipe.ascension.AscensionRecipe;
import net.wither.er.recipe.ascension.AscensionRecipeListener;
import org.jetbrains.annotations.NotNull;

public class WeaponAscension implements IRecipeCategory<AscensionRecipeListener.AscensionEntry> {
    public final static ResourceLocation UID = ResourceLocation.parse("er:weapon_ascension");
    public final static ResourceLocation TEXTURE = ResourceLocation.parse("er:textures/screens/weapon_enhance_jei.png");
    private final IDrawable background;
    private final IDrawable icon;

    public WeaponAscension(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 160, 64);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ErModBlocks.ELEMENT_ANVIL.get().asItem()));
    }

    @Override
    public mezz.jei.api.recipe.@NotNull RecipeType<AscensionRecipeListener.AscensionEntry> getRecipeType() {
        return ErJeiPlugin.WeaponAscensionType;
    }

    @Override
    public @NotNull Component getTitle() {
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
    public void draw(AscensionRecipeListener.@NotNull AscensionEntry recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AscensionRecipeListener.AscensionEntry recipe, @NotNull IFocusGroup focuses) {
        IRecipeSlotBuilder build = builder.addSlot(RecipeIngredientRole.INPUT, 17, 21);
        ItemStack keyItem = new ItemStack(recipe.key()) ;
        if(keyItem.getComponents().has(DataComponentsRegister.WEAPON_LEVEL.get())) {
            WeaponLevelData data = keyItem.get(DataComponentsRegister.WEAPON_LEVEL.get());
            keyItem.update(DataComponentsRegister.WEAPON_LEVEL.get(), data, t -> WeaponLevelData.create(recipe.ascension(), true)) ;
        }
        build.addItemStack(keyItem) ;

        build = builder.addSlot(RecipeIngredientRole.INPUT, 71, 3);
        ItemStack moraBag = new ItemStack(ErModItems.MORA_BAG.get()).copy();
        moraBag.set(DataComponentsRegister.MORA_BAG.get(), recipe.recipe().getMora());
        build.addItemStack(moraBag) ;

        for(int i = 0 ; i < 3 ; i ++) {
            AscensionRecipe.Input input = recipe.recipe().getInput(i) ;
            if(input != null)
                builder.addSlot(RecipeIngredientRole.INPUT, 53 + 18 * i, 39).addItemStack(new ItemStack(input.getItem(), recipe.recipe().getInput(i).getCount()));
        }

        ItemStack resultItem = new ItemStack(recipe.key()) ;
        if(resultItem.getComponents().has(DataComponentsRegister.WEAPON_LEVEL.get())) {
            WeaponLevelData data = resultItem.get(DataComponentsRegister.WEAPON_LEVEL.get());
            resultItem.update(DataComponentsRegister.WEAPON_LEVEL.get(), data, t -> WeaponLevelData.create(recipe.ascension() + 1, false)) ;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 21).addItemStack(resultItem);


    }
}
