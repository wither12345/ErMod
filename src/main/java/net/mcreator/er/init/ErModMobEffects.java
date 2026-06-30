/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import net.mcreator.er.potion.VisionCoolDownMobEffect;
import net.mcreator.er.potion.SuperconductMobEffect;
import net.mcreator.er.potion.QuickenMobEffect;
import net.mcreator.er.potion.DisorderOmenMobEffect;
import net.mcreator.er.potion.AdventureHealingMobEffect;
import net.mcreator.er.ErMod;

public class ErModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ErMod.MODID);
	public static final RegistryObject<MobEffect> QUICKEN = REGISTRY.register("quicken", QuickenMobEffect::new);
	public static final RegistryObject<MobEffect> SUPERCONDUCT = REGISTRY.register("superconduct", SuperconductMobEffect::new);
	public static final RegistryObject<MobEffect> VISION_COOL_DOWN = REGISTRY.register("vision_cool_down", VisionCoolDownMobEffect::new);
	public static final RegistryObject<MobEffect> DISORDER_OMEN = REGISTRY.register("disorder_omen", DisorderOmenMobEffect::new);
	public static final RegistryObject<MobEffect> ADVENTURE_HEALING = REGISTRY.register("adventure_healing", AdventureHealingMobEffect::new);
}