package net.wither.er.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.wither.er.world.inventory.AlchemyGuiMenu;

import java.util.function.Supplier;

public record AlchemyStageSwitchMessage(AlchemyGuiMenu.Stage stage) {
    public AlchemyStageSwitchMessage(FriendlyByteBuf buffer) {
        this(buffer.readBoolean() ? AlchemyGuiMenu.Stage.CONVERTING : AlchemyGuiMenu.Stage.CRAFTING);
    }

    public static void buffer(AlchemyStageSwitchMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.stage() == AlchemyGuiMenu.Stage.CONVERTING);
    }

    public static void handle(final AlchemyStageSwitchMessage data, final Supplier<NetworkEvent.Context> contextSupplier) {
        Player player = contextSupplier.get().getSender();
        if (player != null && player.containerMenu instanceof AlchemyGuiMenu menu) {
            menu.switchStage(data.stage);
        }
        contextSupplier.get().setPacketHandled(true);
    }
}
