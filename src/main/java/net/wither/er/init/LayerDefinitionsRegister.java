package net.wither.er.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.client.models.WhopperflowerModel;
import net.wither.er.client.renderer.LinkMechanismBaseRenderer;
import net.wither.er.client.renderer.LunarChargedCloudRenderer;
import net.wither.er.client.renderer.LunarCrystallizeRenderer;
import net.wither.er.client.renderer.whopperflower.CryoSpikeRenderer;
import net.wither.er.client.renderer.whopperflower.CryoWhopperflowerProjectileRenderer;
import net.wither.er.client.renderer.whopperflower.PyroHomingRobRenderer;
import net.wither.er.client.renderer.whopperflower.PyroWhopperflowerBallRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LayerDefinitionsRegister {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LunarChargedCloudRenderer.LAYER_LOCATION, LunarChargedCloudRenderer::createBodyLayer);
        event.registerLayerDefinition(LunarCrystallizeRenderer.LAYER_LOCATION, LunarCrystallizeRenderer::createBodyLayer);
        event.registerLayerDefinition(WhopperflowerModel.PYRO, WhopperflowerModel::createBodyLayer);
        event.registerLayerDefinition(WhopperflowerModel.CRYO, WhopperflowerModel::createBodyLayer);
        event.registerLayerDefinition(PyroWhopperflowerBallRenderer.LAYER_LOCATION, PyroWhopperflowerBallRenderer::createBodyLayer);
        event.registerLayerDefinition(PyroHomingRobRenderer.LAYER_LOCATION, PyroHomingRobRenderer::createBodyLayer);
        event.registerLayerDefinition(CryoSpikeRenderer.LAYER_LOCATION, CryoSpikeRenderer::createBodyLayer);
        event.registerLayerDefinition(CryoWhopperflowerProjectileRenderer.LAYER_LOCATION, CryoWhopperflowerProjectileRenderer::createBodyLayer);
        event.registerLayerDefinition(LinkMechanismBaseRenderer.LAYER_LOCATION, LinkMechanismBaseRenderer::createBodyLayer);
    }
}
