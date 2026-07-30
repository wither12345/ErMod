package net.wither.er.advancements.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.wither.er.init.AdvancementTriggerRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class ReactionTrigger extends SimpleCriterionTrigger<ReactionTrigger.Instance> {
    public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
        Codec.STRING.fieldOf("name").forGetter(Instance::name)
    ).apply(instance, Instance::new));

    @Override
    public @NotNull Codec<Instance> codec() {
        return CODEC;
    }

    public void trigger(@NotNull ServerPlayer player, String name) {
        super.trigger(player, criticalDamageTriggerInstance -> criticalDamageTriggerInstance.test(name));
    }

    public record Instance(Optional<ContextAwarePredicate> player, String name) implements SimpleInstance {
        public static Criterion<Instance> instance(ContextAwarePredicate player, String name) {
            return AdvancementTriggerRegister.REACTION.get().createCriterion(new Instance(Optional.of(player), name));
        }

        public boolean test(String name) {
            return Objects.equals(this.name, name);
        }
    }
}
