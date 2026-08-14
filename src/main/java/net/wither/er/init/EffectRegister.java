package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.effect.EffectHarmful;
import net.wither.er.effect.OverloadEffect;

public class EffectRegister {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, ErMod.MODID);

    public static final Holder<MobEffect> OVERLOADED = REGISTRY.register("overloaded", OverloadEffect::new);
    public static final Holder<MobEffect> GAMBLER_CD = REGISTRY.register("gambler_cd", EffectHarmful::new);
}
