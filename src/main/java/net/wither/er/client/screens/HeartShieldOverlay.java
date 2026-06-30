package net.wither.er.client.screens;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.shield.ErShield;
import net.wither.er.shield.HeartChangingShield;

import java.util.List;

@EventBusSubscriber(value = {Dist.CLIENT})
public class HeartShieldOverlay {
	@SubscribeEvent
	public static void HeartTypeChanging(PlayerHeartTypeEvent event) {
		if (event.getEntity() instanceof ErEntityInterface entity) {
			List<ErShield> shields = entity.er$getShields();
			for (ErShield shield : shields) {
				if (shield instanceof HeartChangingShield) {
					event.setType(((HeartChangingShield) shield).getType());
					return;
				}
			}
		}
	}
}