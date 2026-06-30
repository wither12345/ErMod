package net.wither.er.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.wither.er.client.renderer.RenderDamageAmount;
import net.wither.er.client.screens.ErOverlay;

import java.util.function.Supplier;

public record DamageDisplayMessage(int damage , int id , int color , boolean critical){
    public DamageDisplayMessage(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readBoolean());
    }

    public static void buffer(DamageDisplayMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.damage());
        buffer.writeInt(message.id());
        buffer.writeInt(message.color());
        buffer.writeBoolean(message.critical());
    }

    public static void handle(final DamageDisplayMessage data, final Supplier<NetworkEvent.Context> contextSupplier) {
        if(Minecraft.getInstance().level == null)
            return;
        Entity entity = Minecraft.getInstance().level.getEntity(data.id());
        NetworkEvent.Context context = contextSupplier.get();
        if(context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            context.enqueueWork(() -> {
                if (entity == Minecraft.getInstance().player) {
                    ErOverlay.updateDamage(data.damage(), data.color());
                } else if (entity != null && entity.level() instanceof ClientLevel) {
                    RenderDamageAmount.addDamage(data.damage(), data.color(), entity.getX() + Math.random() - 0.5, entity.getY() + 2, entity.getZ() + Math.random() - 0.5, data.critical());
                }
            });
        }
        context.setPacketHandled(true);
    }
}