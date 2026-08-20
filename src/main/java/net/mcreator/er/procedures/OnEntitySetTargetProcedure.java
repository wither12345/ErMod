package net.mcreator.er.procedures;

import net.mcreator.er.entity.FlamingFlowerEntity;
import net.mcreator.er.entity.MistFlowerEntity;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.entity.slimes.DendroSlime;
import net.wither.er.entity.outcrop.Blossom;

@Mod.EventBusSubscriber
public class OnEntitySetTargetProcedure {
	@SubscribeEvent
	public static void onEntitySetsAttackTarget(LivingChangeTargetEvent event) {
		if (event.getNewTarget() instanceof Blossom || event.getNewTarget() instanceof MistFlowerEntity || event.getNewTarget() instanceof FlamingFlowerEntity
				|| (event.getNewTarget() instanceof DendroSlime slime && slime.onGround() && slime.isHiding())) {
			event.setCanceled(true);
		}
	}
}