package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.client.renderer.damge.RenderDamageAmount;
import net.wither.er.client.screens.ErOverlay;

public record DamageDisplayMessage(int damage , int id , int color , boolean critical, RenderDamageAmount.DamageDisplayType damageDisplayType)  implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DamageDisplayMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "damage_display"));
    public static final StreamCodec<ByteBuf, DamageDisplayMessage> STREAM_CODEC = StreamCodec.composite(
            // damage
            ByteBufCodecs.VAR_INT, DamageDisplayMessage::damage,
            // id
            ByteBufCodecs.VAR_INT, DamageDisplayMessage::id,
            // color
            ByteBufCodecs.VAR_INT, DamageDisplayMessage::color,
            ByteBufCodecs.BOOL, DamageDisplayMessage::critical,
            ByteBufCodecs.INT, DamageDisplayMessage::getType,
            DamageDisplayMessage::new);

    public DamageDisplayMessage(int damage , int id , int color , boolean critical, int type){
        this(damage, id, color, critical, RenderDamageAmount.DamageDisplayType.values()[type]);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private int getType(){
        return damageDisplayType.ordinal();
    }

    public static void handle(final DamageDisplayMessage data, final IPayloadContext context) {
        Entity entity = Minecraft.getInstance().level.getEntity(data.id());
        if(entity == context.player()){
            ErOverlay.updateDamage(data.damage(), data.color());
        }
        else if(entity != null && entity.level() instanceof ClientLevel) {
            context.enqueueWork(() -> {
                RenderDamageAmount.addDamage(data.damage(), data.color(), entity.getX() + Math.random() - 0.5 , entity.getY() + 2, entity.getZ() + Math.random() - 0.5 , data.critical(), data.damageDisplayType());
            });
        }
    }
}