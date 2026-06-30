package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.init.ErModGameRules;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber
public record ErSyncGameRule(boolean running_stamina) implements CustomPacketPayload {
	//true = add shield false = remove shield
	public static final CustomPacketPayload.Type<ErSyncGameRule> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "sync_game_rule"));
	public static final StreamCodec<ByteBuf, ErSyncGameRule> STREAM_CODEC = StreamCodec.composite(
			//entity id
			//shield 
			ByteBufCodecs.BOOL, ErSyncGameRule::running_stamina, ErSyncGameRule::new);
	private static boolean runningStamina = true;

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(final ErSyncGameRule data, final IPayloadContext context) {
		if (!Minecraft.getInstance().level.isClientSide())
			return;
		context.enqueueWork(() -> {
			runningStamina = data.running_stamina;
		});
	}

	public static boolean getRunningStamina() {
		return runningStamina;
	}

	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		if (event.getLevel() instanceof ServerLevel level && level.getLevelData().getGameRules().getBoolean(ErModGameRules.RUNNING_STAMINA_CONSUMABLE) != runningStamina) {
			runningStamina = level.getLevelData().getGameRules().getBoolean(ErModGameRules.RUNNING_STAMINA_CONSUMABLE);
			PacketDistributor.sendToAllPlayers(new ErSyncGameRule(runningStamina));
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		event.getEntity().getAttribute(ErModAttributes.CRIT_DAMAGE).setBaseValue(0.5);
		if (event.getEntity().level() instanceof ServerLevel level && level.getLevelData().getGameRules().getBoolean(ErModGameRules.RUNNING_STAMINA_CONSUMABLE) != runningStamina) {
			runningStamina = level.getLevelData().getGameRules().getBoolean(ErModGameRules.RUNNING_STAMINA_CONSUMABLE);
			PacketDistributor.sendToAllPlayers(new ErSyncGameRule(runningStamina));
		}
	}
}