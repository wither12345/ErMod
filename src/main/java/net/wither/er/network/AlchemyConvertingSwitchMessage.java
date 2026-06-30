package net.wither.er.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.wither.er.world.inventory.AlchemyGuiMenu;

import java.util.function.Supplier;

public record AlchemyConvertingSwitchMessage(boolean isNext)  {
    public AlchemyConvertingSwitchMessage(FriendlyByteBuf buffer) {
        this(buffer.readBoolean());
    }

    public static void buffer(AlchemyConvertingSwitchMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.isNext());
    }

    public static void handle(final AlchemyConvertingSwitchMessage data, final Supplier<NetworkEvent.Context> contextSupplier) {
        Player player = contextSupplier.get().getSender();
        if (player != null && player.containerMenu instanceof AlchemyGuiMenu menu) {
            if (data.isNext())
                menu.nextConverting();
            else
                menu.preConverting();
        }
        contextSupplier.get().setPacketHandled(true);
    }
}
