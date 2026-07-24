package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.shield.*;

import java.util.function.Supplier;

public class ShieldRegistry {
	public static final DeferredRegister<ErShield> SHIELDS = DeferredRegister.create(AdditionalRegistries.SHIELD_REGISTRY, ErMod.MODID);
	public static final Supplier<ErShield> HYDRO_CRYSTALLIZE = SHIELDS.register("hydro_crystallize", HydroCrystallizeShield::new);
	public static final Supplier<ErShield> PYRO_CRYSTALLIZE = SHIELDS.register("pyro_crystallize", PyroCrystallizeShield::new);
	public static final Supplier<ErShield> ELECTRO_CRYSTALLIZE = SHIELDS.register("electro_crystallize", ElectroCrystallizeShield::new);
	public static final Supplier<ErShield> CRYO_CRYSTALLIZE = SHIELDS.register("cryo_crystallize", CryoCrystallizeShield::new);
	public static final Supplier<ErShield> THUNDER_SHIELD = SHIELDS.register("thunder_shield", ThunderShield::new);
    public static final Supplier<ErShield> PYRO_WHOPPERFLOWER = SHIELDS.register("pyro_whopperflower", PyroWhopperflowerShield::new);
    public static final Supplier<ErShield> CRYO_WHOPPERFLOWER = SHIELDS.register("cryo_whopperflower", CryoWhopperflowerShield::new);
}
