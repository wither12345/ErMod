package net.wither.er.client.renderer;

import net.mcreator.er.init.ErModBlockEntities;
import net.mcreator.er.init.ErModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.wither.er.client.renderer.slime.*;
import net.wither.er.client.renderer.whopperflower.*;

@EventBusSubscriber(value = {Dist.CLIENT})
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
        event.registerEntityRenderer(ErModEntities.LUNAR_CLOUD.get(), LunarChargedCloudRenderer::new);
        event.registerEntityRenderer(ErModEntities.LUNAR_CRYSTALLIZE.get(), LunarCrystallizeRenderer::new);
        event.registerEntityRenderer(ErModEntities.LUNAR_CRYSTALLIZE_PROJECTILE.get(), LunarCrystallizeProjectileRenderer::new);
        event.registerEntityRenderer(ErModEntities.PYRO_WHOPPERFLOWER.get(), PyroWhopperflowerRenderer::new);
        event.registerEntityRenderer(ErModEntities.PYRO_FLOWER_BALL.get(), PyroWhopperflowerBallRenderer::new);
        event.registerEntityRenderer(ErModEntities.PYRO_HOMING_ROB.get(), PyroHomingRobRenderer::new);
        event.registerEntityRenderer(ErModEntities.CRYO_WHOPPERFLOWER.get(), CryoWhopperflowerRenderer::new);
        event.registerEntityRenderer(ErModEntities.CRYO_SPIKE.get(), CryoSpikeRenderer::new);
        event.registerEntityRenderer(ErModEntities.CRYO_WHOPPERFLOWER_PROJECTILE.get(), CryoWhopperflowerProjectileRenderer::new);
        event.registerBlockEntityRenderer(ErModBlockEntities.LINK_MECHANISM_ENTITY.get(), LinkMechanismBaseRenderer::new);
        event.registerEntityRenderer(ErModEntities.LINK_MECHANISM_TELPHER.get(), LinkMechanismTelpherRenderer::new);
    }
}
