package net.wither.er.onevent;

import net.mcreator.er.StellaFortunas;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OnPickupExp {
	@SubscribeEvent
	public static void onPickupXp(PlayerXpEvent.PickupXp event) {
		Player player = event.getEntity();
		StellaFortunas.addExptoPlayer(player ,event.getOrb().getValue());
	}
}