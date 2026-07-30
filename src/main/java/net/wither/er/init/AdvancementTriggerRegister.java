package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.advancements.trigger.CriticalDamageTrigger;
import net.wither.er.advancements.trigger.ReactionTrigger;

import java.util.function.Supplier;

public class AdvancementTriggerRegister {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
        DeferredRegister.create(Registries.TRIGGER_TYPE, ErMod.MODID);

    public static final Supplier<CriticalDamageTrigger> CRITICAL_DAMAGE =
            TRIGGER_TYPES.register("critical_damage", CriticalDamageTrigger::new);

    public static final Supplier<PlayerTrigger> ELEMENTAL_HOE = TRIGGER_TYPES.register("elemental_hoe", PlayerTrigger::new);
    public static final Supplier<PlayerTrigger> WHOPPERFLOWER = TRIGGER_TYPES.register("whopperflower", PlayerTrigger::new);

    public static final Supplier<ReactionTrigger> REACTION = TRIGGER_TYPES.register("reaction", ReactionTrigger::new);
}
