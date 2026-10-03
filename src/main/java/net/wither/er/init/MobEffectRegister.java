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

    static {
        if(ModList.get().isLoaded("teyvatdelight")){
            REGISTRY.register("adeptus_temptation", AdeptusTemptation::new);
        }
    }
}
