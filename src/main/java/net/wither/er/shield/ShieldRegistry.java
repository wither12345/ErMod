package net.wither.er.shield;

import net.mcreator.er.ErMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

import static net.mcreator.er.ErMod.MODID;

public class ShieldRegistry {
	public static final ResourceKey<Registry<ErShield>> SHIELD = ResourceKey.createRegistryKey(new ResourceLocation(MODID, "shields"));
	public static final DeferredRegister<ErShield> SHIELDS = DeferredRegister.create(SHIELD, ErMod.MODID);
	public static Supplier<IForgeRegistry<ErShield>> SHIELD_SUPP ;
	public static IForgeRegistry<ErShield> SHIELD_REGISTRY ;

	static {
		ShieldRegistry.SHIELD_SUPP = ShieldRegistry.SHIELDS.makeRegistry(
				() -> new RegistryBuilder<ErShield>()
						.setName(ShieldRegistry.SHIELD.location())
						.setMaxID(256)
		);
	}

	public static final Supplier<ErShield> HYDRO_CTYSTALLIZE = SHIELDS.register("hydro_crystallize", HydroCrystallizeShield::new);
	public static final Supplier<ErShield> PYRO_CTYSTALLIZE = SHIELDS.register("pyro_crystallize", PyroCrystallizeShield::new);
	public static final Supplier<ErShield> ELECTRO_CTYSTALLIZE = SHIELDS.register("electro_crystallize", ElectroCrystallizeShield::new);
	public static final Supplier<ErShield> CRYO_CTYSTALLIZE = SHIELDS.register("cryo_crystallize", CryoCrystallizeShield::new);
	public static final Supplier<ErShield> THUNDER_SHIELD = SHIELDS.register("thunder_shield", ThunderShield::new);
}
