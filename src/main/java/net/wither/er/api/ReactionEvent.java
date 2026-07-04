package net.wither.er.api;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;
import net.wither.er.elements.AuraContainer;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.SingleElementalContainer;

import javax.annotation.Nullable;

public abstract class ReactionEvent extends Event {
    private final AuraContainer container;
    private final ElementSource elementToAdd;
    private final SingleElementalContainer singleElementalContainer ;
    private final EntityHurtEvent.DamageModifier damageModifier ;
    private final @Nullable Entity applier;

    protected ReactionEvent(AuraContainer container, ElementSource elementToAdd, SingleElementalContainer singleElementalContainer, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
        this.container = container;
        this.elementToAdd = elementToAdd;
        this.singleElementalContainer = singleElementalContainer;
        this.damageModifier = damageModifier;
        this.applier = applier;
    }


    public ElementSource getElementToAdd() {
        return elementToAdd;
    }

    public EntityHurtEvent.DamageModifier getDamageModifier() {
        return damageModifier;
    }

    public AuraContainer getContainer() {
        return container;
    }

    public @Nullable Entity getApplier() {
        return applier;
    }

    public SingleElementalContainer getSingleElementalContainer() {
        return singleElementalContainer;
    }

    public static class Pre extends ReactionEvent {
        public Pre(AuraContainer container, ElementSource elementToAdd, SingleElementalContainer singleElementalContainer, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
            super(container, elementToAdd, singleElementalContainer, damageModifier, applier);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static class Post extends ReactionEvent{
        public Post(AuraContainer container, ElementSource elementToAdd, SingleElementalContainer singleElementalContainer, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
            super(container, elementToAdd, singleElementalContainer, damageModifier, applier);
        }
    }
}
