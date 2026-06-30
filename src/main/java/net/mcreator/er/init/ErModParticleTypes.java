/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.mcreator.er.ErMod;

public class ErModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, ErMod.MODID);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> P_DENDRO_EXPLOSION = REGISTRY.register("p_dendro_explosion", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PYRO_PARTICLE = REGISTRY.register("pyro_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HYDRO_PARTICLE = REGISTRY.register("hydro_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_HYDRO_PARTICLE = REGISTRY.register("small_hydro_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_ELECTRO_PARTICLE = REGISTRY.register("small_electro_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_PYRO_PARTICLE = REGISTRY.register("small_pyro_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_CRYO_PARTICLE = REGISTRY.register("small_cryo_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_DENDRO_PARTICLE = REGISTRY.register("small_dendro_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_ANEMO_PARTICLE = REGISTRY.register("small_anemo_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_GEO_PARTICLE = REGISTRY.register("small_geo_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ANEMO_EXPLOSION = REGISTRY.register("anemo_explosion", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ANEMO_VORTEX = REGISTRY.register("anemo_vortex", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CRYO_VORTEX = REGISTRY.register("cryo_vortex", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PYRO_VORTEX = REGISTRY.register("pyro_vortex", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HYDRO_VORTEX = REGISTRY.register("hydro_vortex", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ELECTRO_VORTEX = REGISTRY.register("electro_vortex", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOSSOM_OF_WEALTH_PARTICLE = REGISTRY.register("blossom_of_wealth_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOSSOM_OMEN_PARTICLE = REGISTRY.register("blossom_omen_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOSSOM_OF_REVELATION_PARTICLE = REGISTRY.register("blossom_of_revelation_particle", () -> new SimpleParticleType(false));
}