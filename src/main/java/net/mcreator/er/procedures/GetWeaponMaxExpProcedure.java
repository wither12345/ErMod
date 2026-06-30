package net.mcreator.er.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

public class GetWeaponMaxExpProcedure {
	public static double execute(ItemStack itemstack) {
		double multi = 0;
		if (itemstack.is(ItemTags.create(new ResourceLocation("er:five_star_weapon")))) {
			multi = 1;
		} else {
			multi = 0.2;
		}
		if (itemstack.getOrCreateTag().getDouble("level") >= 80) {
			return Math.ceil((Math.pow(itemstack.getOrCreateTag().getDouble("level"), 2) * 100 - itemstack.getOrCreateTag().getDouble("level") * 15128.9 + 575654.6) * multi) * 25;
		} else if (itemstack.getOrCreateTag().getDouble("level") >= 70) {
			return Math.ceil((Math.pow(itemstack.getOrCreateTag().getDouble("level"), 2) * 0.95 + itemstack.getOrCreateTag().getDouble("level") * 24.8 + 81) * multi) * 25;
		} else if (itemstack.getOrCreateTag().getDouble("level") >= 60) {
			return Math.ceil(((Math.pow(itemstack.getOrCreateTag().getDouble("level"), 2) * 0.8 + itemstack.getOrCreateTag().getDouble("level") * 36.42) - 370) * multi) * 25;
		} else if (itemstack.getOrCreateTag().getDouble("level") >= 50) {
			return Math.ceil(((Math.pow(itemstack.getOrCreateTag().getDouble("level"), 2) * 0.8 + itemstack.getOrCreateTag().getDouble("level") * 27) - 109.8) * multi) * 25;
		} else if (itemstack.getOrCreateTag().getDouble("level") >= 40) {
			return Math.ceil(((Math.pow(itemstack.getOrCreateTag().getDouble("level"), 2) * 0.8 + itemstack.getOrCreateTag().getDouble("level") * 25.3) - 89.7) * multi) * 25;
		} else if (itemstack.getOrCreateTag().getDouble("level") >= 20) {
			return Math.ceil(((Math.pow(itemstack.getOrCreateTag().getDouble("level"), 2) * 0.8 + itemstack.getOrCreateTag().getDouble("level") * 20.6) - 21) * multi) * 25;
		}
		return Math.ceil((Math.pow(itemstack.getOrCreateTag().getDouble("level"), 2) * 0.9 + itemstack.getOrCreateTag().getDouble("level") * 14.8 + 21.2) * multi) * 25;
	}
}