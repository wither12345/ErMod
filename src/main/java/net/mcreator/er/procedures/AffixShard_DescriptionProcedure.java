package net.mcreator.er.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

import net.mcreator.er.init.ErModItems;

public class AffixShard_DescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		String attr_name = "";
		String NBT_name = "";
		if (BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.parse((itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("attribute_name")))) != null) {
			NBT_name = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("attribute_name");
			attr_name = BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.parse(NBT_name)).getDescriptionId();
		}
		if (itemstack.getItem() == ErModItems.MAIN_AFFIX_SHARD.get()) {
			return Component.translatable(attr_name).getString() + ":"
					+ new java.text.DecimalFormat(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("multiplied") || (NBT_name).equals("er:crit_damage") || (NBT_name).equals("er:crit_rate") ? "##.#%" : "##.#")
							.format(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("attribute_count"));
		}
		return Component.translatable(attr_name).getString() + ":"
				+ new java.text.DecimalFormat(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("multiplied") || (NBT_name).equals("er:crit_damage") || (NBT_name).equals("er:crit_rate") ? "##.#%" : "##.#")
						.format(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("attribute_count"))
				+ " x " + new java.text.DecimalFormat("##.#").format(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("count_multiplier"));
	}
}