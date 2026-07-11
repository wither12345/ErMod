package net.wither.er.init;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.wither.er.client.renderer.LunarChargedCloudRenderer;

@EventBusSubscriber(Dist.CLIENT)
public class LayerDefinitionsRegister {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LunarChargedCloudRenderer.LAYER_LOCATION, LunarChargedCloudRenderer::createBodyLayer);
    }
}
