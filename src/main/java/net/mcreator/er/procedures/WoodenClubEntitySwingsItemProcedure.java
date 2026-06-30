package net.mcreator.er.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;

public class WoodenClubEntitySwingsItemProcedure {
	public static void execute(LevelAccessor world, ItemStack itemstack) {
		if (itemstack.getOrCreateTag().getDouble("Pyro") >= 1) {
			itemstack.getOrCreateTag().putDouble("Pyro", (itemstack.getOrCreateTag().getDouble("Pyro") - 1));
			{
				ItemStack _ist = itemstack;
				if (_ist.hurt(4, RandomSource.create(), null)) {
					_ist.shrink(1);
					_ist.setDamageValue(0);
				}
			}
		}
	}
}