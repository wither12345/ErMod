package net.wither.er.item.data.weapon;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;
import net.wither.er.elements.AuraContainer;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ReactionAbility {
    void onReaction(AuraContainer container, ElementSource elementToAdd, Element elementReacted, EntityHurtEvent.DamageModifier damageModifier, @NotNull Entity applier, int level);
}
