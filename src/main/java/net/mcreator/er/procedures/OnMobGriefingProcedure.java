package net.mcreator.er.procedures;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OnMobGriefingProcedure {
	@SubscribeEvent
	public static void onEntityGrief(EntityMobGriefingEvent event) {
		if(event.getEntity() instanceof Projectile projectile){
			if (projectile.getOwner() != null &&projectile.getOwner().getPersistentData().contains("BlossomOwner"))
				event.setResult(Event.Result.DENY);
		}
		else if (event.getEntity().getPersistentData().contains("BlossomOwner"))
			event.setResult(Event.Result.DENY);
	}
}