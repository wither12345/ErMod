package net.wither.er.network;

import net.mcreator.er.ErMod;
import net.mcreator.er.StellaFortunas;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber()
public record SkillMessage(int eventType, boolean keyType) implements CustomPacketPayload {
	public static final Type<SkillMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "key_key_r"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SkillMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SkillMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeBoolean(message.keyType);
	}, (RegistryFriendlyByteBuf buffer) -> new SkillMessage(buffer.readInt(), buffer.readBoolean()));

	@Override
	public Type<SkillMessage> type() {
		return TYPE;
	}

	public static void handleData(final SkillMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				pressAction(context.player(), message.eventType, message.keyType);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void pressAction(Player entity, int type, boolean keyType) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 0) {
			onDown(entity, keyType);
		}
		if (type == 1) {
			release(entity, keyType);
		}
	}

	public static void onDown(Player player, boolean keyType) {
		ItemStack stack = player.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna;
		if (stack.getItem() instanceof StellaFortunas SF) {
			if (keyType) {
				player.getPersistentData().putBoolean("burstPressing", true);
				SF.ElementalBurstStart(player);
				return;
			}
			player.getPersistentData().putBoolean("skillPressing", true);
			SF.ElementalSkillStart(player);
		}
	}

	public static void release(Player player, boolean keyType) {
		ItemStack stack = player.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna;
		if (stack.getItem() instanceof StellaFortunas SF) {
			if (keyType) {
				player.getPersistentData().putBoolean("burstPressing", false);
				SF.ElementalBurstEnd(player);
				return;
			}
			player.getPersistentData().putBoolean("skillPressing", false);
			SF.ElementalSkillEnd(player);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ErMod.addNetworkMessage(SkillMessage.TYPE, SkillMessage.STREAM_CODEC, SkillMessage::handleData);
	}
}