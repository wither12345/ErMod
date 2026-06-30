package net.mcreator.er.block.grower;

import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.util.RandomSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.data.worldgen.features.FeatureUtils;

public class CuihuaSaplingTreeGrower extends AbstractMegaTreeGrower {
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean hasFlower) {
		if (randomSource.nextFloat() < 0.1) {
			if (hasFlower)
				return FeatureUtils.createKey("er:cuihua_tree");
			return FeatureUtils.createKey("er:cuihua_tree");
		}
		return hasFlower ? FeatureUtils.createKey("er:cuihua_tree") : FeatureUtils.createKey("er:cuihua_tree");
	}

	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource randomSource) {
		return (randomSource.nextFloat() < 0.1) ? FeatureUtils.createKey("er:cuihua_tree") : FeatureUtils.createKey("er:cuihua_tree");
	}
}