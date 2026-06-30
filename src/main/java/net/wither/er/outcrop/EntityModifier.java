package net.wither.er.outcrop;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.Entity;

public abstract class EntityModifier {
    abstract public void read(JsonElement element);
    abstract public void apply(Entity entity , int level);
    abstract public EntityModifier copy() ;
}
