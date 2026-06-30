package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncLevelData(int entityID, int level) implements CustomPacketPayload {
	//true = add shield false = remove shield
	public static final CustomPacketPayload.Type<SyncLevelData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "level_data"));
	public static final StreamCodec<ByteBuf, SyncLevelData> STREAM_CODEC = StreamCodec.composite(
			//entity id
			ByteBufCodecs.VAR_INT, SyncLevelData::entityID,
			//shield 
			ByteBufCodecs.VAR_INT, SyncLevelData::level, SyncLevelData::new);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(final SyncLevelData data, final IPayloadContext context) {
		Entity entity = Minecraft.getInstance().level.getEntity(data.entityID());
		if (!Minecraft.getInstance().level.isClientSide() || entity == null)
			return;
		context.enqueueWork(() -> {
			entity.getPersistentData().putInt("erLevel", data.level);
		});
	}
}