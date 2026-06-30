package net.wither.er.loottables;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegisterLootFunction {
    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTION_TYPES =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, ErMod.MODID);

    public static final RegistryObject<LootItemFunctionType> OUTCROP_LEVEL_FUNCTION =
            LOOT_FUNCTION_TYPES.register("outcrop_level_count", () -> new LootItemFunctionType(new OutcropLevelFunction.Serializer()));
    public static final RegistryObject<LootItemFunctionType> ARTIFACT_INIT_FUNCTION =
            LOOT_FUNCTION_TYPES.register("artifact_init", () -> new LootItemFunctionType(new ArtifactInitFunction.Serializer()));
}
