package net.wither.er.combat;

import net.mcreator.er.ErMod;
import net.mcreator.er.StellaFortunas;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;

import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class CombatAnimation {
	private static int presstime = -1;

	/*
	@SubscribeEvent
	public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
		Minecraft.getInstance().player.getCapability(ErCombatVariables.PLAYER_VARIABLES).ifPresent(cap -> {
			cap.syncPlayerVariables(event.getEntity());
		});
	}

	 */

	@SubscribeEvent
	public static void onAttack(AttackEntityEvent event) {
		if (SwingAnimation(event.getEntity()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
		ErMod.PACKET_HANDLER.sendToServer(new AnimationMessage());
	}

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (presstime > 0) {
			presstime--;
			if (presstime == 0)
				ErMod.PACKET_HANDLER.sendToServer(new ChargedAttackMessage());
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
		ErItemVariables.PlayerVariables var = entity.getCapability(ErItemVariables.PLAYER_VARIABLES).orElse(new ErItemVariables.PlayerVariables());
		ItemStack stellaFortuna = var.Stella_Fortuna;

		if (stellaFortuna == null)
			return false;
		Item item = stellaFortuna.getItem();
		ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
		if (item instanceof StellaFortunas SFitem && SFitem.hasAnimation(entity)) {
			float speed = (float) entity.getAttribute(Attributes.ATTACK_SPEED).getValue();
			int animationId = vars.animationId;
			if (vars.animationTime > SFitem.getFinishTick(entity, animationId, speed) || !entity.onGround()) {
				return true;
			}
			if (vars.animationTime > 0 && vars.animationId < SFitem.getMaxCombo(entity))
				vars.animationId += 1;
			else
				vars.animationId = 0;
			entity.setSprinting(false);
			vars.animationTime = SFitem.getAnimationTick(entity, animationId, speed);
			vars.syncAnimation(entity);
			//vars.syncPlayerVariables(entity);
			return true;
		}
		return false;
	}

	public record AnimationMessage() {
		public AnimationMessage(FriendlyByteBuf buffer) {
			this();
		}

		public static void buffer(AnimationMessage message, FriendlyByteBuf buffer) {
		}

		public static void handleData(final AnimationMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			ServerPlayer player = context.getSender();
			if (player == null)
				return;
			context.enqueueWork(() -> {
				SwingAnimation(player);
			});
			context.setPacketHandled(true);

		}
	}

	@Mod.EventBusSubscriber()
	public record ChargedAttackMessage() {
		public ChargedAttackMessage(FriendlyByteBuf buffer) {
			this();
		}

		public static void buffer(ChargedAttackMessage message, FriendlyByteBuf buffer) {
		}
		public static void handleData(final ChargedAttackMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			ServerPlayer player = context.getSender();
			if (player == null)
				return;
            context.enqueueWork(() -> {
				player.getPersistentData().putBoolean("WaitingChargeAttack", true);
            });
			context.setPacketHandled(true);
        }
	}
}