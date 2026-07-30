package net.wither.er.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.wither.er.player.ErPlayerInterface;

import java.util.function.Supplier;

public record MoraSelectData(int index) {
	public MoraSelectData(FriendlyByteBuf buf){
		this(buf.readInt());
	}

	public static void buffer(MoraSelectData message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.index());
	}

	public static void handle(final MoraSelectData data, final Supplier<NetworkEvent.Context> contextSupplier) {
        Player player = contextSupplier.get().getSender();
        ((ErPlayerInterface)player).er$setMoraIndex(data.index());
	}
}