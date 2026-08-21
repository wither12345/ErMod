package net.wither.er.entity.outcrop;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class EntityModifier {
    abstract public void apply(Entity entity , int level);


    @FunctionalInterface
    public interface Builder{
        @NotNull EntityModifier build(JsonElement element);
    }
}
