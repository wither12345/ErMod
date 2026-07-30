package net.wither.er.advancements.trigger;

import com.google.gson.JsonObject;
import net.mcreator.er.ErMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class CriticalDamageTrigger extends SimpleCriterionTrigger<CriticalDamageTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(ErMod.MODID, "critical_damage");

    @Override
    protected @NotNull Instance createInstance(@NotNull JsonObject jsonObject, @NotNull ContextAwarePredicate contextAwarePredicate, @NotNull DeserializationContext deserializationContext) {
        return new Instance(contextAwarePredicate, jsonObject.get("dmg").getAsFloat());
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, float damage) {
        this.trigger(player, (instance) -> instance.test(damage));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final float damageAmount;
        public Instance(ContextAwarePredicate player, float damageAmount) {
            super(CriticalDamageTrigger.ID, player);
            this.damageAmount = damageAmount;
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject obj = super.serializeToJson(context);
            obj.addProperty("dmg", damageAmount);
            return super.serializeToJson(context);
        }

        public boolean test(float damageAmount) {
            return this.damageAmount <= damageAmount;
        }
    }
}
