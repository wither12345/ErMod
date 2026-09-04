package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.effect.EffectHarmful;
import net.wither.er.effect.InstructorBless;
import net.wither.er.effect.Overloaded;
import net.wither.er.effect.TinyMiracleEffect;

public class MobEffectRegister {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, ErMod.MODID);

    public static final RegistryObject<MobEffect> OVERLOADED = REGISTRY.register("overloaded", Overloaded::new);
    public static final RegistryObject<MobEffect> GAMBLER_CD = REGISTRY.register("gambler_cd", EffectHarmful::new);
    public static final RegistryObject<MobEffect> INSTRUCTOR_BLESS = REGISTRY.register("instructor_bless", InstructorBless::new);
    public static final RegistryObject<MobEffect> TINY_MIRACLE = REGISTRY.register("tiny_miracle", TinyMiracleEffect::new);
}
