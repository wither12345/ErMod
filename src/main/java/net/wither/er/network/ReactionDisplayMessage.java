package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.client.renderer.damge.RenderDamageAmount;
import net.wither.er.elements.Element;
import net.wither.er.elements.IAuraContainerOwner;
import net.wither.er.elements.Reaction;
import org.jetbrains.annotations.NotNull;

public record ReactionDisplayMessage(Reaction reaction, double x, double y, double z, int color, RenderDamageAmount.Display display) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ReactionDisplayMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "reaction_display"));
    public static final StreamCodec<ByteBuf, ReactionDisplayMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ReactionDisplayMessage::reactionType,
            ByteBufCodecs.DOUBLE, ReactionDisplayMessage::x,
            ByteBufCodecs.DOUBLE, ReactionDisplayMessage::y,
            ByteBufCodecs.DOUBLE, ReactionDisplayMessage::z,
            ByteBufCodecs.VAR_INT, ReactionDisplayMessage::color,
            ByteBufCodecs.VAR_INT, ReactionDisplayMessage::displayType,
            ReactionDisplayMessage::new);

    private ReactionDisplayMessage(int reaction, double x, double y, double z, int color, int displayType){
        this(Reaction.values()[reaction], x, y, z, color, RenderDamageAmount.Display.values()[displayType]);
    }

    public ReactionDisplayMessage(Reaction reaction, IAuraContainerOwner owner, Element.Category category, RenderDamageAmount.Display display){
        this(reaction, owner.er$getPosition().x(), owner.er$getPosition().y(), owner.er$getPosition().z(), category.getColor(), display);
    }

    public ReactionDisplayMessage(Reaction reaction, IAuraContainerOwner owner, int color, RenderDamageAmount.Display display){
        this(reaction, owner.er$getPosition().x(), owner.er$getPosition().y(), owner.er$getPosition().z(), color, display);
    }

    private int reactionType() {
        return reaction.ordinal();
    }

    private int displayType() {
        return display.ordinal();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ReactionDisplayMessage data, final IPayloadContext context) {
        context.enqueueWork(() -> RenderDamageAmount.addReaction(data.reaction(), data.x() + Math.random() - 0.5, data.y() + Math.random() + 2, data.z() + Math.random() - 0.5, data.color(), data.display()));
    }
}
