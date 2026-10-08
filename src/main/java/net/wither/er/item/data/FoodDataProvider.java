package net.wither.er.item.data;

import net.minecraft.world.item.ItemStack;

public interface FoodDataProvider {
    ErFoodData getFoodData(ItemStack itemStack);
    boolean replacePotion();
}
