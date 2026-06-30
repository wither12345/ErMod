package net.mcreator.er.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;

public class SunsettiaItem extends Item {
	public SunsettiaItem() {
		super(new Item.Properties().food((new FoodProperties.Builder()).nutrition(4).saturationModifier(2.4f).build()));
	}
}