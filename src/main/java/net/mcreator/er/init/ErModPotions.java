/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.registries.Registries;

import net.mcreator.er.ErMod;

public class ErModPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(Registries.POTION, ErMod.MODID);
	public static final DeferredHolder<Potion, Potion> EMPTY_RESISTANCE_POTION = REGISTRY.register("empty_resistance_potion", () -> new Potion());
	public static final DeferredHolder<Potion, Potion> EMPTY_DAMAGE_POTION_TYPE_1 = REGISTRY.register("empty_damage_potion_type_1", () -> new Potion());
	public static final DeferredHolder<Potion, Potion> EMPTY_DAMAGE_POTION_TYPE_2 = REGISTRY.register("empty_damage_potion_type_2", () -> new Potion());
}