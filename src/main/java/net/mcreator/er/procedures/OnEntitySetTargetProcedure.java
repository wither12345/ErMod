package net.mcreator.er.procedures;

import net.wither.er.entity.outcrop.Blossom;
import net.wither.er.entity.slimes.DendroSlime;

import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.mcreator.er.entity.MistFlowerEntity;
import net.mcreator.er.entity.FlamingFlowerEntity;

@EventBusSubscriber
public class OnEntitySetTargetProcedure {
	@SubscribeEvent
	public static void onEntitySetsAttackTarget(LivingChangeTargetEvent event) {
		if (event.getNewAboutToBeSetTarget() instanceof Blossom || event.getNewAboutToBeSetTarget() instanceof MistFlowerEntity || event.getNewAboutToBeSetTarget() instanceof FlamingFlowerEntity
				|| (event.getNewAboutToBeSetTarget() instanceof DendroSlime slime && slime.onGround() && slime.isHiding())) {
			event.setCanceled(true);
		}
	}
}