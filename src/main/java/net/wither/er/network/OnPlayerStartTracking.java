package net.wither.er.network;

import net.mcreator.er.ErMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.wither.er.entity.ErEntityInterface;

@Mod.EventBusSubscriber
public class OnPlayerStartTracking {
	@SubscribeEvent
	public static void OnStartTracking(PlayerEvent.StartTracking event) {
		Entity target = event.getTarget();
		if (target instanceof ErEntityInterface enti && event.getEntity() instanceof ServerPlayer player) {
			enti.er$syncShield(player);
			ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SyncLevelData(target.getId(), target.getPersistentData().getInt("erLevel")));
		}
	}
}