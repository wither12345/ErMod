package net.wither.er.network;

import net.mcreator.er.StellaFortunas;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record StellaFortunaData(int entityID, CompoundTag message) {
	public StellaFortunaData(FriendlyByteBuf buffer) {
		this(buffer.readInt(), buffer.readNbt());
	}

	public static void buffer(StellaFortunaData message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.entityID());
		buffer.writeNbt(message.message());
	}

	public static void handle(final StellaFortunaData data, final Supplier<NetworkEvent.Context> contextSupplier) {
		if(Minecraft.getInstance().level == null)return;
		Entity entity = Minecraft.getInstance().level.getEntity(data.entityID());
		if (!Minecraft.getInstance().level.isClientSide())
			return;
		contextSupplier.get().enqueueWork(() -> {
			if (entity instanceof Player player) {
				ErItemVariables.PlayerVariables vars = player.getCapability(ErItemVariables.PLAYER_VARIABLES).orElseGet(ErItemVariables.PlayerVariables::new);
				if (vars.Stella_Fortuna.getItem() instanceof StellaFortunas fortuna) {
					fortuna.receiveMessage(player, data.message);
				}
			}
		});
		contextSupplier.get().setPacketHandled(true);
	}
}