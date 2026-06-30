package net.wither.er.loottables;

import com.mojang.serialization.Codec;
import net.mcreator.er.ErMod;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class RegisterLootModifier {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ErMod.MODID);

    public static final Supplier<Codec<MobDropLootModifier>> MOB_DROP_LOOT_MODIFIER =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("mob_drop_loot_modifier", () -> MobDropLootModifier.CODEC);

    public static final Supplier<Codec<AddLootModifier>> ADD_LOOT_MODIFIER =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("add_loot_modifier", () -> AddLootModifier.CODEC);
}
