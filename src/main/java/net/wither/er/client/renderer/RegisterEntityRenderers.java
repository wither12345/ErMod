package net.wither.er.client.renderer;

import net.mcreator.er.init.ErModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterEntityRenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ErModEntities.ARC.get(), ArcRenderer::new);
        event.registerEntityRenderer(ErModEntities.HYPERBLOOM.get(), HyperbloomRenderer::new);
        event.registerEntityRenderer(ErModEntities.BLOOM_ENTITY.get(), BloomEntityRenderer::new);
        event.registerEntityRenderer(ErModEntities.ELECTRO_SLIME.get(), ElectroSlimeRenderer::new);
        event.registerEntityRenderer(ErModEntities.GEO_SLIME.get(), GeoSlimeRenderer::new);
        event.registerEntityRenderer(ErModEntities.PYRO_SLIME.get(), PyroSlimeRenderer::new);
        event.registerEntityRenderer(ErModEntities.HYDRO_SLIME.get(), HydroSlimeRenderer::new);
        event.registerEntityRenderer(ErModEntities.CRYO_SLIME.get(), CryoSlimeRenderer::new);
        event.registerEntityRenderer(ErModEntities.DENDRO_SLIME.get(), DendroSlimeRenderer::new);
        event.registerEntityRenderer(ErModEntities.ANEMO_SLIME.get(), AnemoSlimeRenderer::new);
        event.registerEntityRenderer(ErModEntities.ENERGY_ORB.get(), EnergyOrbRenderer::new);
        event.registerEntityRenderer(ErModEntities.LUNAR_CLOUD.get(), LunarChargedCloudRenderer::new);
        event.registerEntityRenderer(ErModEntities.LUNAR_CRYSTALLIZE.get(), LunarCrystallizeRenderer::new);
        event.registerEntityRenderer(ErModEntities.LUNAR_CRYSTALLIZE_PROJECTILE.get(), LunarCrystallizeProjectileRenderer::new);
    }
}
