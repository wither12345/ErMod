package net.wither.er.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncLevelData(int entityID, int level) {
	public SyncLevelData(FriendlyByteBuf buf){
		this(buf.readInt(), buf.readInt());
	}

	public static void buffer(SyncLevelData message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.entityID());
		buffer.writeInt(message.level());
	}

	public static void handle(final SyncLevelData data, final Supplier<NetworkEvent.Context> contextSupplier) {
		if(Minecraft.getInstance().level == null)return;
		Entity entity = Minecraft.getInstance().level.getEntity(data.entityID());
		if (!Minecraft.getInstance().level.isClientSide() || entity == null)
			return;
		contextSupplier.get().enqueueWork(() -> entity.getPersistentData().putInt("erLevel", data.level));
		contextSupplier.get().setPacketHandled(true);
	}
}