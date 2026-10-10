package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.effect.*;

public class MobEffectRegister {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, ErMod.MODID);

    public static final RegistryObject<MobEffect> OVERLOADED = REGISTRY.register("overloaded", Overloaded::new);
    public static final RegistryObject<MobEffect> GAMBLER_CD = REGISTRY.register("gambler_cd", EffectHarmful::new);
    public static final RegistryObject<MobEffect> INSTRUCTOR_BLESS = REGISTRY.register("instructor_bless", InstructorBless::new);
    public static final RegistryObject<MobEffect> TINY_MIRACLE = REGISTRY.register("tiny_miracle", TinyMiracleEffect::new);
    public static final RegistryObject<MobEffect> APPETIZER = REGISTRY.register("appetizer", Appetizer::new);
    public static final RegistryObject<MobEffect> SATISFACTION = REGISTRY.register("satisfaction", Satisfaction::new);
    public static final RegistryObject<MobEffect> TASTED_PHYSICAL = REGISTRY.register("tasted_physical", TastedPhysical::new);
    public static final RegistryObject<MobEffect> TASTED_STRONG = REGISTRY.register("tasted_strong", TastedStrong::new);
    public static final RegistryObject<MobEffect> TASTED_CRITICAL = REGISTRY.register("tasted_critical", TastedCritical::new);
    public static final RegistryObject<MobEffect> TASTED_PROTECTIVE = REGISTRY.register("tasted_protective", TastedProtective::new);
    public static final RegistryObject<MobEffect> ENERGETIC = REGISTRY.register("energetic", Energetic::new);
    public static final RegistryObject<MobEffect> DETERMINATION = REGISTRY.register("determination", Determination::new);
    public static final RegistryObject<MobEffect> GASH = REGISTRY.register("gash", EffectHarmful::new);

    static {
        if(ModList.get().isLoaded("teyvatdelight")){
            REGISTRY.register("adeptus_temptation", AdeptusTemptation::new);
        }
    }
}
