package net.wither.er.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.wither.er.entity.ErEntityInterface;

import java.util.function.Supplier;

public record ErShieldData(int entityID, CompoundTag shield)  {
	public ErShieldData(FriendlyByteBuf buffer) {
		this(buffer.readInt(), buffer.readNbt());
	}

	public static void buffer(ErShieldData message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.entityID());
		buffer.writeNbt(message.shield());
	}

	public static void handle(final ErShieldData data, final Supplier<NetworkEvent.Context> contextSupplier) {
		if(Minecraft.getInstance().level == null)
			return;
		
		Entity entity = Minecraft.getInstance().level.getEntity(data.entityID());
		if (!Minecraft.getInstance().level.isClientSide())
			return;
		if(entity instanceof ErEntityInterface enti)
			enti.er$setShields(data.shield);
		contextSupplier.get().setPacketHandled(true);
	}
}
