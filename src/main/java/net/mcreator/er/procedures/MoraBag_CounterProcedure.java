package net.mcreator.er.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

public class MoraBag_CounterProcedure {
	public static String execute(ItemStack itemstack) {
		return new java.text.DecimalFormat("##").format(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("moras"));
	}
}