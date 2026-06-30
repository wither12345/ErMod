/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.mcreator.er.ErMod;

public class ErModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ErMod.MODID);
	public static final RegistryObject<SimpleParticleType> P_DENDRO_EXPLOSION = REGISTRY.register("p_dendro_explosion", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> PYRO_PARTICLE = REGISTRY.register("pyro_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> HYDRO_PARTICLE = REGISTRY.register("hydro_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SMALL_HYDRO_PARTICLE = REGISTRY.register("small_hydro_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SMALL_ELECTRO_PARTICLE = REGISTRY.register("small_electro_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SMALL_PYRO_PARTICLE = REGISTRY.register("small_pyro_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SMALL_CRYO_PARTICLE = REGISTRY.register("small_cryo_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SMALL_DENDRO_PARTICLE = REGISTRY.register("small_dendro_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SMALL_ANEMO_PARTICLE = REGISTRY.register("small_anemo_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SMALL_GEO_PARTICLE = REGISTRY.register("small_geo_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> ANEMO_EXPLOSION = REGISTRY.register("anemo_explosion", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> ANEMO_VORTEX = REGISTRY.register("anemo_vortex", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> CRYO_VORTEX = REGISTRY.register("cryo_vortex", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> PYRO_VORTEX = REGISTRY.register("pyro_vortex", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> HYDRO_VORTEX = REGISTRY.register("hydro_vortex", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> ELECTRO_VORTEX = REGISTRY.register("electro_vortex", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> BLOSSOM_OF_WEALTH_PARTICLE = REGISTRY.register("blossom_of_wealth_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> BLOSSOM_OMEN_PARTICLE = REGISTRY.register("blossom_omen_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> BLOSSOM_OF_REVELATION_PARTICLE = REGISTRY.register("blossom_of_revelation_particle", () -> new SimpleParticleType(false));
}