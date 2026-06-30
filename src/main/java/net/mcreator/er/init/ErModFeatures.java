/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.core.registries.Registries;

import net.mcreator.er.world.features.LotusHeadFeatureFeature;
import net.mcreator.er.ErMod;

public class ErModFeatures {
	public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, ErMod.MODID);
	public static final DeferredHolder<Feature<?>, Feature<?>> LOTUS_HEAD_FEATURE = REGISTRY.register("lotus_head_feature", LotusHeadFeatureFeature::new);
}