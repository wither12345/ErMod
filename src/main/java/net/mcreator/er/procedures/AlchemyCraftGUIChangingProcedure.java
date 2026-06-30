package net.mcreator.er.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.NonNullList;
import net.wither.er.recipe.crafting.AlchemyCraftingRecipe;

import java.util.stream.Collectors;
import java.util.function.Supplier;
import java.util.Map;
import java.util.List;

public class AlchemyCraftGUIChangingProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
			ItemStack _setstack = (new Object() {
				public ItemStack getResult() {
					if (world instanceof Level _lvl) {
						net.minecraft.world.item.crafting.RecipeManager rm = _lvl.getRecipeManager();
						List<AlchemyCraftingRecipe> recipes = rm.getAllRecipesFor(AlchemyCraftingRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
						for (AlchemyCraftingRecipe recipe : recipes) {
							if (test(_slots, 1, recipe))
								continue;
							if (test(_slots, 2, recipe))
								continue;
							if (test(_slots, 3, recipe))
								continue;
							if (((Slot) _slots.get(0)).getItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("moras") < recipe.getMora())
								continue;
							return recipe.getResultItem(null);
						}
					}
					return ItemStack.EMPTY;
				}
			}.getResult()).copy();
			((Slot) _slots.get(4)).set(_setstack);
			_player.containerMenu.broadcastChanges();
		}
	}

	private static boolean test(Map slots, int index, AlchemyCraftingRecipe recipe) {
		NonNullList<ItemStack> ingredients = recipe.getIngredient();
		Slot slot = (Slot) slots.get(index);
		if (ingredients.size() < index)
			return slot.getItem().getItem() != ItemStack.EMPTY.getItem();
		ItemStack item = ingredients.get(index - 1);
		return slot.getItem().getCount() < item.getCount() || !(item.getItem() == slot.getItem().getItem());
	}
}