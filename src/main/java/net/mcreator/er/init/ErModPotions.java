/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.alchemy.Potion;

import net.mcreator.er.ErMod;

public class ErModPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, ErMod.MODID);
	public static final RegistryObject<Potion> EMPTY_RESISTANCE_POTION = REGISTRY.register("empty_resistance_potion", () -> new Potion());
	public static final RegistryObject<Potion> EMPTY_DAMAGE_POTION_TYPE_1 = REGISTRY.register("empty_damage_potion_type_1", () -> new Potion());
	public static final RegistryObject<Potion> EMPTY_DAMAGE_POTION_TYPE_2 = REGISTRY.register("empty_damage_potion_type_2", () -> new Potion());
}