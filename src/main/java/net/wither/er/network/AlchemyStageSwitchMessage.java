package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.world.inventory.AlchemyGuiMenu;
import org.jetbrains.annotations.NotNull;

public record AlchemyStageSwitchMessage(AlchemyGuiMenu.Stage stage) implements CustomPacketPayload {
    public static final Type<AlchemyStageSwitchMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("er", "alchemy_stage"));

    public static final StreamCodec<ByteBuf, AlchemyStageSwitchMessage> STREAM_CODEC = StreamCodec.composite(AlchemyGuiMenu.Stage.STREAM_CODEC, AlchemyStageSwitchMessage::stage, AlchemyStageSwitchMessage::new) ;

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final AlchemyStageSwitchMessage data, final IPayloadContext context) {
        Player player = context.player();
        if (player.containerMenu instanceof AlchemyGuiMenu menu) {
            menu.switchStage(data.stage);
        }
    }
}
