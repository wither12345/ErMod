package net.wither.er.init;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.wither.er.client.model.WhopperflowerModel;
import net.wither.er.client.renderer.LunarChargedCloudRenderer;
import net.wither.er.client.renderer.LunarCrystallizeRenderer;
import net.wither.er.client.renderer.whopperflower.CryoSpikeRenderer;
import net.wither.er.client.renderer.whopperflower.CryoWhopperflowerProjectileRenderer;
import net.wither.er.client.renderer.whopperflower.PyroHomingRobRenderer;
import net.wither.er.client.renderer.whopperflower.PyroWhopperflowerBallRenderer;

@EventBusSubscriber(Dist.CLIENT)
public class LayerDefinitionsRegister {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LunarChargedCloudRenderer.LAYER_LOCATION, LunarChargedCloudRenderer::createBodyLayer);
        event.registerLayerDefinition(LunarCrystallizeRenderer.LAYER_LOCATION, LunarCrystallizeRenderer::createBodyLayer);
        event.registerLayerDefinition(WhopperflowerModel.LAYER_LOCATION, WhopperflowerModel::createBodyLayer);
        event.registerLayerDefinition(PyroWhopperflowerBallRenderer.LAYER_LOCATION, PyroWhopperflowerBallRenderer::createBodyLayer);
        event.registerLayerDefinition(PyroHomingRobRenderer.LAYER_LOCATION, PyroHomingRobRenderer::createBodyLayer);
        event.registerLayerDefinition(CryoSpikeRenderer.LAYER_LOCATION, CryoSpikeRenderer::createBodyLayer);
        event.registerLayerDefinition(CryoWhopperflowerProjectileRenderer.LAYER_LOCATION, CryoWhopperflowerProjectileRenderer::createBodyLayer);
    }
}
