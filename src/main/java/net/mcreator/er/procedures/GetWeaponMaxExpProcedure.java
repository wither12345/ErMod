package net.mcreator.er.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.component.DataComponents;

public class GetWeaponMaxExpProcedure {
	public static double execute(ItemStack itemstack) {
		double multi = 0;
		if (itemstack.is(ItemTags.create(ResourceLocation.parse("er:five_star_weapon")))) {
			multi = 1;
		} else {
			multi = 0.2;
		}
		if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") >= 80) {
			return Math.ceil((Math.pow(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level"), 2) * 100
					- itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") * 15128.9 + 575654.6) * multi) * 25;
		} else if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") >= 70) {
			return Math.ceil((Math.pow(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level"), 2) * 0.95
					+ itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") * 24.8 + 81) * multi) * 25;
		} else if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") >= 60) {
			return Math.ceil(
					((Math.pow(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level"), 2) * 0.8 + itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") * 36.42)
							- 370) * multi)
					* 25;
		} else if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") >= 50) {
			return Math
					.ceil(((Math.pow(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level"), 2) * 0.8 + itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") * 27)
							- 109.8) * multi)
					* 25;
		} else if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") >= 40) {
			return Math.ceil(
					((Math.pow(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level"), 2) * 0.8 + itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") * 25.3)
							- 89.7) * multi)
					* 25;
		} else if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") >= 20) {
			return Math.ceil(
					((Math.pow(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level"), 2) * 0.8 + itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") * 20.6)
							- 21) * multi)
					* 25;
		}
		return Math.ceil(
				(Math.pow(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level"), 2) * 0.9 + itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") * 14.8 + 21.2)
						* multi)
				* 25;
	}
}