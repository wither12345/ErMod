package net.mcreator.er.procedures;

import net.minecraft.world.item.ItemStack;

public class HuntersBow_PullingProcedure {
	public static double execute(ItemStack itemstack) {
		return itemstack.getOrCreateTag().getDouble("pulling");
	}
}