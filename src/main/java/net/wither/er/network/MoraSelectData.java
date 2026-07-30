package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.player.ErPlayerInterface;
import org.jetbrains.annotations.NotNull;

public record MoraSelectData(int index) implements CustomPacketPayload{
    public static final CustomPacketPayload.Type<MoraSelectData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "mora"));
    public static final StreamCodec<ByteBuf, MoraSelectData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MoraSelectData::index,
            MoraSelectData::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final MoraSelectData data, final IPayloadContext context) {
        Player player = context.player();
        ((ErPlayerInterface)player).er$setMoraIndex(data.index());
    }
}
