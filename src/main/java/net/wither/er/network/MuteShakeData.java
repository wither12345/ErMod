package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.player.ILocalPlayer;
import org.jetbrains.annotations.NotNull;

public record MuteShakeData() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MuteShakeData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "mate_shake"));

    public static final StreamCodec<ByteBuf, MuteShakeData> STREAM_CODEC = StreamCodec.unit(new MuteShakeData());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final MuteShakeData data, final IPayloadContext context) {
        if(Minecraft.getInstance().player instanceof ILocalPlayer localPlayer)
            localPlayer.er$setMuteShake();
    }
}
