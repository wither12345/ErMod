package net.wither.er.combat;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;

public interface DamageModifierInterface {
    EntityHurtEvent.DamageModifier er$getModifier() ;
    void er$reset();
    boolean er$oriEmpty();
    Entity er$getTarget();
    void er$setTarget(Entity entity);
}
