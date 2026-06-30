package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.shield.*;

import java.util.function.Supplier;

public class ShieldRegistry {
	public static final DeferredRegister<ErShield> SHIELDS = DeferredRegister.create(AdditionalRegistries.SHIELD_REGISTRY, ErMod.MODID);
	public static final Supplier<ErShield> HYDRO_CTYSTALLIZE = SHIELDS.register("hydro_crystallize", HydroCrystallizeShield::new);
	public static final Supplier<ErShield> PYRO_CTYSTALLIZE = SHIELDS.register("pyro_crystallize", PyroCrystallizeShield::new);
	public static final Supplier<ErShield> ELECTRO_CTYSTALLIZE = SHIELDS.register("electro_crystallize", ElectroCrystallizeShield::new);
	public static final Supplier<ErShield> CRYO_CTYSTALLIZE = SHIELDS.register("cryo_crystallize", CryoCrystallizeShield::new);
	public static final Supplier<ErShield> THUNDER_SHIELD = SHIELDS.register("thunder_shield", ThunderShield::new);
}
