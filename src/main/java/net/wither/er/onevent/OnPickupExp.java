package net.wither.er.onevent;

import net.mcreator.er.StellaFortunas;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

@EventBusSubscriber
public class OnPickupExp {
	@SubscribeEvent
	public static void onPickupXp(PlayerXpEvent.PickupXp event) {
		Player player = event.getEntity();
		StellaFortunas.addExptoPlayer(player ,event.getOrb().getValue());
	}
}