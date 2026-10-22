package net.wither.er.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.wither.er.player.ILocalPlayer;

import java.util.function.Supplier;

public record MuteShakeData() {
    public MuteShakeData(FriendlyByteBuf buffer){
        this();
    }

    public static void buffer(MuteShakeData message, FriendlyByteBuf buffer) {
    }

    public static void handle(final MuteShakeData data, final Supplier<NetworkEvent.Context> contextSupplier) {
        if (Minecraft.getInstance().player instanceof ILocalPlayer localPlayer)
            localPlayer.er$setMuteShake();
        contextSupplier.get().setPacketHandled(true);
    }
}
