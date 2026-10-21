package net.wither.er.client.renderer.damge;

import net.mcreator.er.ERClientConfig;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.elements.Element;
import net.wither.er.elements.IAuraContainerOwner;
import net.wither.er.elements.Reaction;
import net.wither.er.network.ReactionDisplayMessage;

public class ReactionDisplay extends DisplayMessage {
    private final String displayText;

    protected ReactionDisplay(Reaction reaction, long addedTick, double x, double y, double z, int color, RenderDamageAmount.Display type) {
        super(addedTick, x, y, z, color, type);
        this.displayText = reaction.getTranslated();
    }

    @Override
    public String getDisplayText() {
        return displayText;
    }

    @Override
    public float getSize(float partialTick, long nowTime) {
        return Math.min(0.02f, (nowTime + partialTick - this.added_tick) * 0.02f * ERClientConfig.REACTION_SCALE.get().floatValue());
    }

    public static void trigger(Reaction reaction, IAuraContainerOwner owner, Element.Category category, RenderDamageAmount.Display display){
        PacketDistributor.sendToAllPlayers(new ReactionDisplayMessage(reaction, owner, category, display));
    }

    public static void trigger(Reaction reaction, IAuraContainerOwner owner, int color, RenderDamageAmount.Display display){
        PacketDistributor.sendToAllPlayers(new ReactionDisplayMessage(reaction, owner, color, display));
    }
}
