package net.wither.er.network;

import net.mcreator.er.ErMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.item.StellaFortuna;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber()
public record SkillMessage(int eventType, boolean keyType) implements CustomPacketPayload {
	public static final Type<SkillMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "key_key_r"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SkillMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SkillMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeBoolean(message.keyType);
	}, (RegistryFriendlyByteBuf buffer) -> new SkillMessage(buffer.readInt(), buffer.readBoolean()));

	@Override
	public @NotNull Type<SkillMessage> type() {
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
		if (type == 0) {
			onDown(entity, keyType);
		}
		if (type == 1) {
			release(entity, keyType);
		}
	}

	public static void onDown(Player player, boolean keyType) {
		ItemStack stack = player.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna;
		if (stack.getItem() instanceof StellaFortuna SF && SF.hasSkill()) {
			if (keyType) {
				player.getPersistentData().putBoolean("burstPressing", true);
				SF.elementalBurstStart(player);
				return;
			}
			player.getPersistentData().putBoolean("skillPressing", true);
			SF.elementalSkillStart(player);
		}
	}

	public static void release(Player player, boolean keyType) {
		ItemStack stack = player.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna;
		if (stack.getItem() instanceof StellaFortuna SF && SF.hasSkill()) {
			if (keyType) {
				player.getPersistentData().putBoolean("burstPressing", false);
				SF.elementalBurstEnd(player);
				return;
			}
			player.getPersistentData().putBoolean("skillPressing", false);
			SF.elementalSkillEnd(player);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ErMod.addNetworkMessage(SkillMessage.TYPE, SkillMessage.STREAM_CODEC, SkillMessage::handleData);
	}
}