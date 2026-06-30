package net.mcreator.er.procedures;

import net.minecraft.world.item.ItemStack;

public class RarityGemstone_CountProcedure {
	public static double execute(ItemStack itemstack) {
		return itemstack.getCount();
	}
}