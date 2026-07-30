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

import java.util.Optional;

public class CriticalDamageTrigger extends SimpleCriterionTrigger<CriticalDamageTrigger.Instance> {
    public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
        Codec.FLOAT.fieldOf("dmg").forGetter(Instance::damageAmount)
    ).apply(instance, Instance::new));

    @Override
    public @NotNull Codec<Instance> codec() {
        return CODEC;
    }

    public void trigger(@NotNull ServerPlayer player, float dmg) {
        super.trigger(player, criticalDamageTriggerInstance -> criticalDamageTriggerInstance.test(dmg));
    }

    public record Instance(Optional<ContextAwarePredicate> player, float damageAmount) implements SimpleInstance {
        public static Criterion<Instance> instance(ContextAwarePredicate player, float damageAmount) {
            return AdvancementTriggerRegister.CRITICAL_DAMAGE.get().createCriterion(new Instance(Optional.of(player), damageAmount));
        }

        public boolean test(float damageAmount) {
            return this.damageAmount <= damageAmount;
        }
    }
}
