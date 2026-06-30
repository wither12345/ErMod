/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

import net.mcreator.er.enchantment.*;
import net.mcreator.er.ErMod;

public class ErModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ErMod.MODID);
	public static final RegistryObject<Enchantment> ELECTRO_INFUSION_ENCHANTMENT = REGISTRY.register("electro_infusion_enchantment", ElectroInfusionEnchantmentEnchantment::new);
	public static final RegistryObject<Enchantment> ANEMO_INFUSION_ENCHANTMENT = REGISTRY.register("anemo_infusion_enchantment", AnemoInfusionEnchantmentEnchantment::new);
	public static final RegistryObject<Enchantment> CRYO_INFUSION_ENCHANTMENT = REGISTRY.register("cryo_infusion_enchantment", CryoInfusionEnchantmentEnchantment::new);
	public static final RegistryObject<Enchantment> DENDRO_INFUSION_ENCHANTMENT = REGISTRY.register("dendro_infusion_enchantment", DendroInfusionEnchantmentEnchantment::new);
	public static final RegistryObject<Enchantment> GEO_INFUSION_ENCHANTMENT = REGISTRY.register("geo_infusion_enchantment", GeoInfusionEnchantmentEnchantment::new);
	public static final RegistryObject<Enchantment> HYDRO_INFUSION_ENCHANTMENT = REGISTRY.register("hydro_infusion_enchantment", HydroInfusionEnchantmentEnchantment::new);
	public static final RegistryObject<Enchantment> PYRO_INFUSION_ENCHANTMENT = REGISTRY.register("pyro_infusion_enchantment", PyroInfusionEnchantmentEnchantment::new);
	public static final RegistryObject<Enchantment> ELEMENTAL_MASTER = REGISTRY.register("elemental_master", ElementalMasterEnchantment::new);
	public static final RegistryObject<Enchantment> HARD = REGISTRY.register("hard", HardEnchantment::new);
	public static final RegistryObject<Enchantment> WEALTH = REGISTRY.register("wealth", WealthEnchantment::new);
	public static final RegistryObject<Enchantment> GREED = REGISTRY.register("greed", GreedEnchantment::new);
}