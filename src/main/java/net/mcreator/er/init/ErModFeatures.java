/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.levelgen.feature.Feature;

import net.mcreator.er.world.features.LotusHeadFeatureFeature;
import net.mcreator.er.ErMod;

@Mod.EventBusSubscriber
public class ErModFeatures {
	public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, ErMod.MODID);
	public static final RegistryObject<Feature<?>> LOTUS_HEAD_FEATURE = REGISTRY.register("lotus_head_feature", LotusHeadFeatureFeature::new);
}