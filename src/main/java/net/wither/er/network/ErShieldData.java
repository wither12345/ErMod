package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.entity.ErEntityInterface;

public record ErShieldData(int entityID, CompoundTag shield) implements CustomPacketPayload {
	//true = add shield false = remove shield
	public static final CustomPacketPayload.Type<ErShieldData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "shield_data"));
	public static final StreamCodec<ByteBuf, ErShieldData> STREAM_CODEC = StreamCodec.composite(
			//entity id
			ByteBufCodecs.VAR_INT, ErShieldData::entityID,
			//shield 
			ByteBufCodecs.COMPOUND_TAG, ErShieldData::shield, ErShieldData::new);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(final ErShieldData data, final IPayloadContext context) {
		Entity entity = Minecraft.getInstance().level.getEntity(data.entityID());
		if (!Minecraft.getInstance().level.isClientSide())
			return;
		context.enqueueWork(() -> {
			if(entity instanceof ErEntityInterface enti)
				enti.er$setShields(data.shield);
		});
	}
}
