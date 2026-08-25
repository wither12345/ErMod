package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.mcreator.er.init.ErModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LeyLineLeapData(int x, int y, int z) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<LeyLineLeapData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "ley_line_leap"));
	public static final StreamCodec<ByteBuf, LeyLineLeapData> STREAM_CODEC = StreamCodec.composite(
			// x
			ByteBufCodecs.VAR_INT, LeyLineLeapData::x,
			// y
			ByteBufCodecs.VAR_INT, LeyLineLeapData::y,
			// z
			ByteBufCodecs.VAR_INT, LeyLineLeapData::z, LeyLineLeapData::new);
	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(final LeyLineLeapData data, final IPayloadContext context) {
		Player entity = context.player();
		Level world = entity.level();
		ItemStack item = entity.getMainHandItem();
		MapItemSavedData mapData = MapItem.getSavedData(item, world);
		if (mapData == null || mapData.dimension != world.dimension())
			return;
		context.enqueueWork(() -> {
			if ((world.getBlockState(BlockPos.containing(data.x, data.y, data.z))).getBlock() == ErModBlocks.STATUEOF_THE_SEVEN_CORE.get()
					|| (world.getBlockState(BlockPos.containing(data.x, data.y, data.z))).getBlock() == ErModBlocks.TELEPORT_WAYPOINT.get()) {
				entity.teleportTo(data.x - 1, data.y - 1, data.z - 1);
			}
		});
	}
}