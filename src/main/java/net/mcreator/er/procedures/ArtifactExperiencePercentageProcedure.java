package net.mcreator.er.procedures;

import net.minecraft.world.item.ItemStack;

import net.mcreator.er.item.Artifact;

public class ArtifactExperiencePercentageProcedure {
	public static double execute(ItemStack itemstack) {
		if (itemstack.getItem() instanceof Artifact) {
			return Math.min(1, itemstack.getOrCreateTag().getDouble("experience") / ((600 + 175 * itemstack.getOrCreateTag().getDouble("level")) * (itemstack.getOrCreateTag().getDouble("rarity") + 1)));
		}
		return 0;
	}
}