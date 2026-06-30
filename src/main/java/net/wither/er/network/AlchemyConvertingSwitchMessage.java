package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.world.inventory.AlchemyGuiMenu;
import org.jetbrains.annotations.NotNull;

public record AlchemyConvertingSwitchMessage(boolean isNext) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AlchemyConvertingSwitchMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "alchemy_converting"));

    public static final StreamCodec<ByteBuf, AlchemyConvertingSwitchMessage> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, AlchemyConvertingSwitchMessage::isNext, AlchemyConvertingSwitchMessage::new);

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final AlchemyConvertingSwitchMessage data, final IPayloadContext context) {
        Player player = context.player();
        if (player.containerMenu instanceof AlchemyGuiMenu menu) {
            if(data.isNext())
                menu.nextConverting();
            else
                menu.preConverting();
        }
    }
}
