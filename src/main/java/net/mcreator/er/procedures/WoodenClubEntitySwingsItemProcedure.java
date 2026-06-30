package net.mcreator.er.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.component.DataComponents;

public class WoodenClubEntitySwingsItemProcedure {
	public static void execute(LevelAccessor world, ItemStack itemstack) {
		if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("Pyro") >= 1) {
			{
				final String _tagName = "Pyro";
				final double _tagValue = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("Pyro") - 1);
				CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
			}
			if (world instanceof ServerLevel _level) {
				itemstack.hurtAndBreak(4, _level, null, _stkprov -> {
				});
			}
		}
	}
}