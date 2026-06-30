package net.mcreator.er.procedures;

import net.minecraft.world.item.ItemStack;

public class MoraBag_CounterProcedure {
	public static String execute(ItemStack itemstack) {
		return new java.text.DecimalFormat("##").format(itemstack.getOrCreateTag().getDouble("moras"));
	}
}