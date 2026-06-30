/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.mcreator.er.client.particle.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ErModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(ErModParticleTypes.P_DENDRO_EXPLOSION.get(), PDendroExplosionParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.PYRO_PARTICLE.get(), PyroParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.HYDRO_PARTICLE.get(), HydroParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.SMALL_HYDRO_PARTICLE.get(), SmallHydroParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.SMALL_ELECTRO_PARTICLE.get(), SmallElectroParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.SMALL_PYRO_PARTICLE.get(), SmallPyroParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.SMALL_CRYO_PARTICLE.get(), SmallCryoParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.SMALL_DENDRO_PARTICLE.get(), SmallDendroParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.SMALL_ANEMO_PARTICLE.get(), SmallAnemoParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.SMALL_GEO_PARTICLE.get(), SmallGeoParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.ANEMO_EXPLOSION.get(), AnemoExplosionParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.ANEMO_VORTEX.get(), AnemoVortexParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.CRYO_VORTEX.get(), CryoVortexParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.PYRO_VORTEX.get(), PyroVortexParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.HYDRO_VORTEX.get(), HydroVortexParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.ELECTRO_VORTEX.get(), ElectroVortexParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.BLOSSOM_OF_WEALTH_PARTICLE.get(), BlossomOfWealthParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.BLOSSOM_OMEN_PARTICLE.get(), BlossomOmenParticleParticle::provider);
		event.registerSpriteSet(ErModParticleTypes.BLOSSOM_OF_REVELATION_PARTICLE.get(), BlossomOfRevelationParticleParticle::provider);
	}
}