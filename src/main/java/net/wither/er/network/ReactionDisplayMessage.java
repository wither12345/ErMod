package net.wither.er.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.wither.er.client.renderer.damage.RenderDamageAmount;
import net.wither.er.elements.Element;
import net.wither.er.elements.IAuraContainerOwner;
import net.wither.er.elements.Reaction;

import java.util.function.Supplier;

public record ReactionDisplayMessage(Reaction reaction, double x, double y, double z, int color, RenderDamageAmount.Display display){
    public ReactionDisplayMessage(FriendlyByteBuf buffer) {
        this(Reaction.values()[buffer.readInt()], buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt(), RenderDamageAmount.Display.values()[buffer.readInt()]);
    }

    public ReactionDisplayMessage(Reaction reaction, IAuraContainerOwner owner, Element.Category category, RenderDamageAmount.Display display){
        this(reaction, owner.er$getPosition().x(), owner.er$getPosition().y(), owner.er$getPosition().z(), category.getColor(), display);
    }

    public ReactionDisplayMessage(Reaction reaction, IAuraContainerOwner owner, int color, RenderDamageAmount.Display display){
        this(reaction, owner.er$getPosition().x(), owner.er$getPosition().y(), owner.er$getPosition().z(), color, display);
    }

    public static void buffer(ReactionDisplayMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.reaction().ordinal());
        buffer.writeDouble(message.x());
        buffer.writeDouble(message.y());
        buffer.writeDouble(message.z());
        buffer.writeInt(message.color());
        buffer.writeInt(message.display().ordinal());
    }

    public static void handle(final ReactionDisplayMessage data, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> RenderDamageAmount.addReaction(data.reaction(), data.x() + Math.random() - 0.5, data.y() + Math.random() + 2, data.z() + Math.random() - 0.5, data.color(), data.display()));
        context.setPacketHandled(true);
    }
}