package net.wither.er.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.entity.ErEntityInterface;

@EventBusSubscriber
public class OnPlayerStartTracking {
	@SubscribeEvent
	public static void OnStartTracking(PlayerEvent.StartTracking event) {
		Entity target = event.getTarget();
		if (target instanceof ErEntityInterface enti && event.getEntity() instanceof ServerPlayer player) {
			enti.er$syncShield(player);
			PacketDistributor.sendToPlayer(player, new SyncLevelData(target.getId(), target.getPersistentData().getInt("erLevel")));
		}
	}
}