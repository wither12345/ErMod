package net.wither.er.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.wither.er.world.inventory.ArtifactTransmuterGuiMenu;

import java.util.function.Supplier;

public record ArtifactTransmuterMessage() {
    public ArtifactTransmuterMessage(FriendlyByteBuf buffer){
        this();
    }

    public static void buffer(ArtifactTransmuterMessage message, FriendlyByteBuf buffer) {
    }
    
    public static void handle(final ArtifactTransmuterMessage data, final Supplier<NetworkEvent.Context> contextSupplier) {
        Player player = contextSupplier.get().getSender();
        if (player != null && player.containerMenu instanceof ArtifactTransmuterGuiMenu menu) {
            contextSupplier.get().enqueueWork(menu::enhance);
        }
        contextSupplier.get().setPacketHandled(true);
    }
}
