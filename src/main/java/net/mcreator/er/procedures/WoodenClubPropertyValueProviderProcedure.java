package net.mcreator.er.procedures;

import net.minecraft.world.item.ItemStack;

public class WoodenClubPropertyValueProviderProcedure {
	public static double execute(ItemStack itemstack) {
		return itemstack.getOrCreateTag().getDouble("Pyro");
	}
}