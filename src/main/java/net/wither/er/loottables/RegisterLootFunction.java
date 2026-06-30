package net.wither.er.loottables;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegisterLootFunction {
    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPES =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, ErMod.MODID);

    public static final Supplier<LootItemFunctionType<OutcropLevelFunction>> OUTCROP_LEVEL_FUNCTION =
            LOOT_FUNCTION_TYPES.register("outcrop_level_count", () -> new LootItemFunctionType(OutcropLevelFunction.CODEC));
    public static final Supplier<LootItemFunctionType<OutcropLevelFunction>> ARTIFACT_INIT_FUNCTION =
            LOOT_FUNCTION_TYPES.register("artifact_init", () -> new LootItemFunctionType(ArtifactInitFunction.CODEC));
}
