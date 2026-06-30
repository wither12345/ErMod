package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.mcreator.er.StellaFortunas;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StellaFortunaData(int entityID, CompoundTag message) implements CustomPacketPayload {
	//true = add shield false = remove shield
	public static final CustomPacketPayload.Type<StellaFortunaData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "stella_fortuna_data"));
	public static final StreamCodec<ByteBuf, StellaFortunaData> STREAM_CODEC = StreamCodec.composite(
			//entity id
			ByteBufCodecs.VAR_INT, StellaFortunaData::entityID,
			//shield 
			ByteBufCodecs.COMPOUND_TAG, StellaFortunaData::message, StellaFortunaData::new);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(final StellaFortunaData data, final IPayloadContext context) {
		Entity entity = Minecraft.getInstance().level.getEntity(data.entityID());
		if (!Minecraft.getInstance().level.isClientSide())
			return;
		context.enqueueWork(() -> {
			if (entity instanceof Player player) {
				ErItemVariables.PlayerVariables vars = player.getData(ErItemVariables.PLAYER_VARIABLES);
				if (vars.Stella_Fortuna.getItem() instanceof StellaFortunas fortuna) {
					fortuna.receiveMessage(player, data.message);
				}
			}
		});
	}
}