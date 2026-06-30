package net.mcreator.er.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

import net.mcreator.er.item.Artifact;

public class ArtifactExperiencePercentageProcedure {
	public static double execute(ItemStack itemstack) {
		if (itemstack.getItem() instanceof Artifact) {
			return Math.min(1, itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("experience")
					/ ((600 + 175 * itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level")) * (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("rarity") + 1)));
		}
		return 0;
	}
}