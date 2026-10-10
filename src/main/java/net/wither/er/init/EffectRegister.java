package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.effect.*;

public class EffectRegister {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, ErMod.MODID);

    public static final Holder<MobEffect> OVERLOADED = REGISTRY.register("overloaded", OverloadEffect::new);
    public static final Holder<MobEffect> GAMBLER_CD = REGISTRY.register("gambler_cd", EffectHarmful::new);
    public static final Holder<MobEffect> INSTRUCTOR_BLESS = REGISTRY.register("instructor_bless", InstructorBless::new);
    public static final Holder<MobEffect> TINY_MIRACLE = REGISTRY.register("tiny_miracle", TinyMiracleEffect::new);
    public static final Holder<MobEffect> LUNAR_BLESS = REGISTRY.register("lunar_bless", LunarBless::new);
    public static final Holder<MobEffect> APPETIZER = REGISTRY.register("appetizer", Appetizer::new);
    public static final Holder<MobEffect> SATISFACTION = REGISTRY.register("satisfaction", Satisfaction::new);
    public static final Holder<MobEffect> TASTED_PHYSICAL = REGISTRY.register("tasted_physical", TastedPhysical::new);
    public static final Holder<MobEffect> TASTED_STRONG = REGISTRY.register("tasted_strong", TastedStrong::new);
    public static final Holder<MobEffect> TASTED_CRITICAL = REGISTRY.register("tasted_critical", TastedCritical::new);
    public static final Holder<MobEffect> TASTED_PROTECTIVE = REGISTRY.register("tasted_protective", TastedProtective::new);
    public static final Holder<MobEffect> ENERGETIC = REGISTRY.register("energetic", Energetic::new);
    public static final Holder<MobEffect> DETERMINATION = REGISTRY.register("determination", Determination::new);
    public static final Holder<MobEffect> GASH = REGISTRY.register("gash", EffectHarmful::new);

    static {
        if(ModList.get().isLoaded("teyvatdelight")){
            REGISTRY.register("adeptus_temptation", AdeptusTemptation::new);
        }
    }
}
