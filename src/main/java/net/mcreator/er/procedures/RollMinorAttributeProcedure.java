package net.mcreator.er.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.component.DataComponents;

public class RollMinorAttributeProcedure {
	public static boolean execute(ItemStack itemstack) {
		String attr_name = "";
		boolean mult = false;
		boolean find = false;
		double attr_count = 0;
		double sort = 0;
		for (int index0 = 0; index0 < 4; index0++) {
			if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble(("minor_attribute_count_" + new java.text.DecimalFormat("###").format(index0))) <= 0) {
				while (true) {
					mult = false;
					if (Math.random() <= 0.1111111) {
						attr_name = "minecraft:generic.max_health";
						attr_count = 5;
					} else if (Math.random() <= 0.125) {
						attr_name = "minecraft:generic.armor";
						attr_count = 0.0729;
						mult = true;
					} else if (Math.random() <= 0.142857) {
						attr_name = "minecraft:generic.armor";
						attr_count = 23.15;
					} else if (Math.random() <= 0.16666) {
						attr_name = "er:elemental_mastery";
						attr_count = 23.31;
					} else if (Math.random() <= 0.2) {
						attr_name = "er:energy_recharge";
						attr_count = 0.0648;
						mult = true;
					} else if (Math.random() <= 0.25) {
						attr_name = "er:crit_damage";
						attr_count = 0.0777;
						mult = true;
					} else if (Math.random() <= 0.33333) {
						attr_name = "minecraft:generic.attack_damage";
						attr_count = 0.0583;
						mult = true;
					} else if (Math.random() <= 0.5) {
						attr_name = "minecraft:generic.max_health";
						attr_count = 0.0583;
						mult = true;
					} else {
						attr_name = "minecraft:generic.attack_damage";
						attr_count = 3;
					}
					find = true;
					if ((itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("main_attribute_name")).equals(attr_name)
							&& itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("main_attribute_multiplied") == mult) {
						find = false;
					}
					for (int index2 = 0; index2 < 4; index2++) {
						if ((itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(("minor_attribute_name_" + new java.text.DecimalFormat("###").format(index2)))).equals(attr_name)
								&& itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean(("minor_attribute_multiplied_" + new java.text.DecimalFormat("###").format(index2))) == mult) {
							find = false;
						}
					}
					if (find) {
						{
							final String _tagName = ("minor_attribute_name_" + new java.text.DecimalFormat("###").format(index0));
							final String _tagValue = attr_name;
							CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
						}
						{
							final String _tagName = ("minor_attribute_count_" + new java.text.DecimalFormat("###").format(index0));
							final double _tagValue = attr_count;
							CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
						}
						{
							final String _tagName = ("minor_attribute_multiplied_" + new java.text.DecimalFormat("###").format(index0));
							final boolean _tagValue = mult;
							CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putBoolean(_tagName, _tagValue));
						}
						{
							final String _tagName = ("minor_count_multiplier_" + new java.text.DecimalFormat("###").format(index0));
							final double _tagValue = 1;
							CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
						}
						break;
					}
				}
				return true;
			}
		}
		sort = Mth.nextInt(RandomSource.create(), 0, 3);
		{
			final String _tagName = ("minor_count_multiplier_" + new java.text.DecimalFormat("###").format(sort));
			final double _tagValue = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble(("minor_count_multiplier_" + new java.text.DecimalFormat("###").format(sort))) + 1);
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
		}
		return false;
	}
}