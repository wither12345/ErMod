package net.wither.er.loottables;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LootConditionRegister {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES =
        DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, ErMod.MODID);

    public static final Supplier<LootItemConditionType> MOB_DROP =
            LOOT_CONDITION_TYPES.register("mob_drop", () -> new LootItemConditionType(MobDropLootCondition.CODEC));
}
