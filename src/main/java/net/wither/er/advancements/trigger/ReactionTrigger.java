package net.wither.er.advancements.trigger;

import com.google.gson.JsonObject;
import net.mcreator.er.ErMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReactionTrigger extends SimpleCriterionTrigger<ReactionTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(ErMod.MODID, "reaction");

    @Override
    protected @NotNull Instance createInstance(@NotNull JsonObject jsonObject, @NotNull ContextAwarePredicate contextAwarePredicate, @NotNull DeserializationContext deserializationContext) {
        return new Instance(contextAwarePredicate, jsonObject.get("name").getAsString());
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, String name) {
        this.trigger(player, (instance) -> instance.test(name));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final String name;
        public Instance(ContextAwarePredicate player, String name) {
            super(ReactionTrigger.ID, player);
            this.name = name;
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject obj = super.serializeToJson(context);
            obj.addProperty("name", name);
            return super.serializeToJson(context);
        }

        public boolean test(String  name) {
            return Objects.equals(this.name, name);
        }
    }
}
