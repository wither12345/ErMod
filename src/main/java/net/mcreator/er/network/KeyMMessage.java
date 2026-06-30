package net.mcreator.er.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.SectionPos;

import net.mcreator.er.procedures.KeyM_DownProcedure;
import net.mcreator.er.ErMod;

@EventBusSubscriber
public record KeyMMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<KeyMMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "key_key_m"));
	public static final StreamCodec<RegistryFriendlyByteBuf, KeyMMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, KeyMMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new KeyMMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<KeyMMessage> type() {
		return TYPE;
	}

	public static void handleData(final KeyMMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				pressAction(context.player(), message.eventType, message.pressedms);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
			return;
		if (type == 0) {

			KeyM_DownProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ErMod.addNetworkMessage(KeyMMessage.TYPE, KeyMMessage.STREAM_CODEC, KeyMMessage::handleData);
	}
}