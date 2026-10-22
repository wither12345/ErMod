package net.wither.er.network;

import net.wither.er.item.fortuna.StellaFortuna;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SkillMessage(int eventType, boolean keyType){
	public SkillMessage(FriendlyByteBuf buf){
		this(buf.readInt(), buf.readBoolean());
	}
	
	public static void buffer(SkillMessage message, FriendlyByteBuf buf){
		buf.writeInt(message.eventType);
		buf.writeBoolean(message.keyType);
	}
	
	public static void handle(final SkillMessage message, final Supplier<NetworkEvent.Context> contextSupplier){
		contextSupplier.get().enqueueWork(() -> pressAction(contextSupplier.get().getSender(), message.eventType, message.keyType));
		contextSupplier.get().setPacketHandled(true);
	}

	public static void pressAction(Player entity, int type, boolean keyType) {
		Level world = entity.level();
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
		ErItemVariables.PlayerVariables vars = player.getCapability(ErItemVariables.PLAYER_VARIABLES).orElseGet(ErItemVariables.PlayerVariables::new);
		ItemStack stack = vars.Stella_Fortuna;
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
		ErItemVariables.PlayerVariables vars = player.getCapability(ErItemVariables.PLAYER_VARIABLES).orElseGet(ErItemVariables.PlayerVariables::new);
		ItemStack stack = vars.Stella_Fortuna;
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
}