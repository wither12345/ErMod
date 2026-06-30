
package net.wither.er.recipe.crafting;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.er.init.ErModJeiPlugin;
import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModBlocks;

import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.constants.VanillaTypes;

public class AlchemyCraftingRecipeCategory implements IRecipeCategory<AlchemyCraftingRecipe> {
	public final static ResourceLocation UID = ResourceLocation.parse("er:alchemy_crafting");
	public final static ResourceLocation TEXTURE = ResourceLocation.parse("er:textures/screens/alchemy_craft_jei.png");
	private final IDrawable background;
	private final IDrawable icon;

	public AlchemyCraftingRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 128, 64);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ErModBlocks.CRAFTING_BENCH.get().asItem()));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<AlchemyCraftingRecipe> getRecipeType() {
		return ErModJeiPlugin.AlchemyCrafting_Type;
	}

	@Override
	public Component getTitle() {
		return Component.literal("Alchemy Crafting");
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
	public void draw(AlchemyCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.background.draw(guiGraphics);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, AlchemyCraftingRecipe recipe, IFocusGroup focuses) {
		IRecipeSlotBuilder build = builder.addSlot(RecipeIngredientRole.INPUT, 10, 6);
		if (!recipe.getIngredient().isEmpty())
			build.addItemStack(recipe.getIngredient().get(0)) ;
		build = builder.addSlot(RecipeIngredientRole.INPUT, 10, 24);
		if (recipe.getIngredient().size() > 1)
			build.addItemStack(recipe.getIngredient().get(1)) ;
		build = builder.addSlot(RecipeIngredientRole.INPUT, 10, 42);
		if (recipe.getIngredient().size() > 2)
			build.addItemStack(recipe.getIngredient().get(2)) ;
		ItemStack moraBag = new ItemStack(ErModItems.MORA_BAG.get()).copy();
		CustomData.update(DataComponents.CUSTOM_DATA, moraBag, tag -> tag.putInt("moras", recipe.getMora()));
		builder.addSlot(RecipeIngredientRole.INPUT, 55, 24).addItemStack(moraBag);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 24).addItemStack(recipe.getResultItem(null));
	}
}
