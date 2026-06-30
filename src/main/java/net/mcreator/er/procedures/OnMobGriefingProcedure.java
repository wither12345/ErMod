package net.mcreator.er.procedures;

import net.mcreator.er.ErMod;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Explosion;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import javax.annotation.Nullable;

@EventBusSubscriber
public class OnMobGriefingProcedure {
	@SubscribeEvent
	public static void onEntityGrief(EntityMobGriefingEvent event) {
		if(event.getEntity() instanceof Projectile projectile){
			if (projectile.getOwner() != null &&projectile.getOwner().getPersistentData().contains("BlossomOwner"))
				event.setCanGrief(false);
		}
		else if (event.getEntity().getPersistentData().contains("BlossomOwner"))
			event.setCanGrief(false);
	}
}