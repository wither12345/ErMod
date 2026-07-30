package net.wither.er.network;

import net.mcreator.er.ErMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ErMod.MODID)
public class VariableHandlers {
    @SubscribeEvent
    public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(ErItemVariables.PLAYER_VARIABLES).ifPresent(capability -> capability.syncToClient(player));
            player.getCapability(ErCombatVariables.PLAYER_VARIABLES).ifPresent(capability -> capability.syncPlayerVariables(player));
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(ErItemVariables.PLAYER_VARIABLES).ifPresent(capability -> capability.syncToClient(player));
            player.getCapability(ErCombatVariables.PLAYER_VARIABLES).ifPresent(capability -> capability.syncPlayerVariables(player));
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(ErItemVariables.PLAYER_VARIABLES).ifPresent(capability -> capability.syncToClient(player));
            player.getCapability(ErCombatVariables.PLAYER_VARIABLES).ifPresent(capability -> capability.syncPlayerVariables(player));
        }
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        event.getOriginal().revive();
        event.getOriginal().getCapability(ErItemVariables.PLAYER_VARIABLES).ifPresent(original -> {
            event.getEntity().getCapability(ErItemVariables.PLAYER_VARIABLES).ifPresent(clone -> {
                clone.Stella_Fortuna = original.Stella_Fortuna;
                clone.Vision = original.Vision;
            });
        });
    }
}
