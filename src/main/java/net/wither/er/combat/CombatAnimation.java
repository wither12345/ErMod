package net.wither.er.combat;

import net.mcreator.er.ErMod;
import net.mcreator.er.StellaFortunas;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;

@EventBusSubscriber
public class CombatAnimation {
	private static int presstime = -1;

	@SubscribeEvent
	public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
		ErCombatVariables.PlayerVariables vars = event.getEntity().getData(ErCombatVariables.PLAYER_VARIABLES);
		vars.syncPlayerVariables(event.getEntity());
	}

	@SubscribeEvent
	public static void onAttack(AttackEntityEvent event) {
		if (SwingAnimation(event.getEntity()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
		PacketDistributor.sendToServer(new AnimationMessage());
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		if (presstime > 0) {
			presstime--;
			if (presstime == 0)
				PacketDistributor.sendToServer(new ChargedAttackMessage());
		}
	}

	@SubscribeEvent
	public static void onMouseButtonPressed(InputEvent.MouseButton.Post event) {
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().screen == null && event.getButton() == 0) {
			if (event.getAction() == 1)
				presstime = 10;
			if (event.getAction() == 0)
				presstime = -1;
		}
	}

	public static boolean SwingAnimation(LivingEntity entity) {
		ItemStack stellaFortuna = entity.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna;
		if (stellaFortuna == null)
			return false;
		Item item = stellaFortuna.getItem();
		ErCombatVariables.PlayerVariables vars = entity.getData(ErCombatVariables.PLAYER_VARIABLES);
		if (item instanceof StellaFortunas SFitem && SFitem.hasAnimation(entity)) {
			float speed = (float) entity.getAttribute(Attributes.ATTACK_SPEED).getValue();
			int combo = vars.animationId;
			if (vars.animationTime > SFitem.getFinishTick(entity, combo, speed) || !entity.onGround()) {
				return true;
			}
			if (vars.animationTime > 0 && vars.animationId < SFitem.getMaxCombo(entity))
				vars.animationId += 1;
			else
				vars.animationId = 0;
			entity.setSprinting(false);
			vars.animationTime = SFitem.getAnimationTick(entity, combo, speed);
            vars.syncWithId(entity, 0b00_0000_0011);
			return true;
		}
		return false;
	}

	@EventBusSubscriber()
	public record AnimationMessage() implements CustomPacketPayload {
		public static final Type<AnimationMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "animation_message"));
		public static final StreamCodec<RegistryFriendlyByteBuf, AnimationMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, AnimationMessage message) -> {
		}, (RegistryFriendlyByteBuf buffer) -> new AnimationMessage());

		@Override
		public Type<AnimationMessage> type() {
			return TYPE;
		}

		public static void handleData(final AnimationMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.SERVERBOUND) {
				context.enqueueWork(() -> {
					if (!context.player().level().hasChunkAt(context.player().blockPosition()))
						return;
					SwingAnimation(context.player());
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}

		@SubscribeEvent
		public static void registerMessage(FMLCommonSetupEvent event) {
			ErMod.addNetworkMessage(AnimationMessage.TYPE, AnimationMessage.STREAM_CODEC, AnimationMessage::handleData);
		}
	}

	@EventBusSubscriber()
	public record ChargedAttackMessage() implements CustomPacketPayload {
		public static final Type<ChargedAttackMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "charged_attack"));
		public static final StreamCodec<RegistryFriendlyByteBuf, ChargedAttackMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ChargedAttackMessage message) -> {
		}, (RegistryFriendlyByteBuf buffer) -> new ChargedAttackMessage());

		@Override
		public Type<ChargedAttackMessage> type() {
			return TYPE;
		}

		public static void handleData(final ChargedAttackMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.SERVERBOUND) {
				context.enqueueWork(() -> {
					if (!context.player().level().hasChunkAt(context.player().blockPosition()))
						return;
					context.player().getPersistentData().putBoolean("WaitingChargeAttack", true);
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}

		@SubscribeEvent
		public static void registerMessage(FMLCommonSetupEvent event) {
			ErMod.addNetworkMessage(ChargedAttackMessage.TYPE, ChargedAttackMessage.STREAM_CODEC, ChargedAttackMessage::handleData);
		}
	}
}