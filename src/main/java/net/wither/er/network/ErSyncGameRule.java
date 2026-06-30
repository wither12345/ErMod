package net.wither.er.network;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.init.ErModGameRules;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

@Mod.EventBusSubscriber
public record ErSyncGameRule(boolean running_stamina) {
	private static boolean runningStamina = true;
	public ErSyncGameRule(FriendlyByteBuf buffer) {
		this(buffer.readBoolean());
	}

	public static void buffer(ErSyncGameRule message, FriendlyByteBuf buffer) {
		buffer.writeBoolean(message.running_stamina());
	}


	public static void handle(final ErSyncGameRule data, final Supplier<NetworkEvent.Context> contextSupplier) {
        if (Minecraft.getInstance().level != null && !Minecraft.getInstance().level.isClientSide()) return;
        contextSupplier.get().enqueueWork(() -> runningStamina = data.running_stamina);
		contextSupplier.get().setPacketHandled(true);
	}

	public static boolean getRunningStamina() {
		return runningStamina;
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.LevelTickEvent event) {
		if (event.level instanceof ServerLevel level && level.getLevelData().getGameRules().getBoolean(ErModGameRules.RUNNING_STAMINA_CONSUMABLE) != runningStamina) {
			runningStamina = level.getLevelData().getGameRules().getBoolean(ErModGameRules.RUNNING_STAMINA_CONSUMABLE);
			ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new ErSyncGameRule(runningStamina));
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		event.getEntity().getAttribute(ErModAttributes.CRIT_DAMAGE.get()).setBaseValue(0.5);
		if (event.getEntity().level() instanceof ServerLevel level && level.getLevelData().getGameRules().getBoolean(ErModGameRules.RUNNING_STAMINA_CONSUMABLE) != runningStamina) {
			runningStamina = level.getLevelData().getGameRules().getBoolean(ErModGameRules.RUNNING_STAMINA_CONSUMABLE);
			ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new ErSyncGameRule(runningStamina));
		}
	}
}