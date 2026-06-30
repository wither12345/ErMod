package net.wither.er.network;

import net.mcreator.er.init.ErModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record LeyLineLeapData(int x, int y, int z) {

	public LeyLineLeapData(FriendlyByteBuf buffer) {
		this(buffer.readInt(), buffer.readInt(), buffer.readInt());
	}

	public static void buffer(LeyLineLeapData message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.x());
		buffer.writeInt(message.y());
		buffer.writeInt(message.z());
	}


	public static void handle(final LeyLineLeapData data, final Supplier<NetworkEvent.Context> contextSupplier) {
		Player entity = contextSupplier.get().getSender();
		if(entity == null)return;
		Level world = entity.level();
		ItemStack item = entity.getMainHandItem();
		MapItemSavedData mapData = MapItem.getSavedData(item, world);
        if (mapData != null && mapData.dimension != world.dimension()) return;
		if ((world.getBlockState(BlockPos.containing(data.x, data.y, data.z))).getBlock() == ErModBlocks.STATUEOF_THE_SEVEN_CORE.get()
				|| (world.getBlockState(BlockPos.containing(data.x, data.y, data.z))).getBlock() == ErModBlocks.TELEPORT_WAYPOINT.get()) {
			entity.teleportTo(data.x - 1, data.y - 1, data.z - 1);
		}
		contextSupplier.get().setPacketHandled(true);
	}
}