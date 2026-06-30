/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

import net.mcreator.er.procedures.CrystallizeShieldEndProcedure;
import net.mcreator.er.potion.VisionCoolDownMobEffect;
import net.mcreator.er.potion.SuperconductMobEffect;
import net.mcreator.er.potion.DisorderOmenMobEffect;
import net.mcreator.er.potion.CrystallizeShieldMobEffect;
import net.mcreator.er.potion.AdventureHealingMobEffect;
import net.mcreator.er.ErMod;

@EventBusSubscriber
public class ErModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, ErMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> SUPERCONDUCT = REGISTRY.register("superconduct", () -> new SuperconductMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> CRYSTALLIZE_SHIELD = REGISTRY.register("crystallize_shield", () -> new CrystallizeShieldMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> VISION_COOL_DOWN = REGISTRY.register("vision_cool_down", () -> new VisionCoolDownMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> DISORDER_OMEN = REGISTRY.register("disorder_omen", () -> new DisorderOmenMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> ADVENTURE_HEALING = REGISTRY.register("adventure_healing", () -> new AdventureHealingMobEffect());

	@SubscribeEvent
	public static void onEffectRemoved(MobEffectEvent.Remove event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			expireEffects(event.getEntity(), effectInstance);
		}
	}

	@SubscribeEvent
	public static void onEffectExpired(MobEffectEvent.Expired event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			expireEffects(event.getEntity(), effectInstance);
		}
	}

	private static void expireEffects(Entity entity, MobEffectInstance effectInstance) {
		if (effectInstance.getEffect().is(CRYSTALLIZE_SHIELD)) {
			CrystallizeShieldEndProcedure.execute(entity.level(), entity);
		}
	}
}