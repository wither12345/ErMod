package net.wither.er.loottables;

import com.mojang.serialization.MapCodec;
import net.mcreator.er.ErMod;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class RegisterLootModifier {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ErMod.MODID);

    public static final Supplier<MapCodec<MobDropLootModifier>> MOB_DROP_LOOT_MODIFIER =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("mob_drop_loot_modifier", () -> MobDropLootModifier.CODEC);
}
