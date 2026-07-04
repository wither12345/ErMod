package net.wither.er.api;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.wither.er.elements.AuraContainer;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.SingleElementalContainer;

import javax.annotation.Nullable;

public class EventListener {
    public static boolean onReactionPre(AuraContainer container, ElementSource elementToAdd, SingleElementalContainer singleElementalContainer, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier){
        ReactionEvent.Pre event = new ReactionEvent.Pre(container, elementToAdd, singleElementalContainer, damageModifier, applier);
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

    public static void onReactionPost(AuraContainer container, ElementSource elementToAdd, SingleElementalContainer singleElementalContainer, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier){
        ReactionEvent.Post event = new ReactionEvent.Post(container, elementToAdd, singleElementalContainer, damageModifier, applier);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
