/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import net.mcreator.er.client.renderer.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ErModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ErModEntities.PYRO_CRYSTALLIZE.get(), PyroCrystallizeRenderer::new);
		event.registerEntityRenderer(ErModEntities.CRYO_CRYSTALLIZE.get(), CryoCrystallizeRenderer::new);
		event.registerEntityRenderer(ErModEntities.ELECTRO_CRYSTALLIZE.get(), ElectroCrystallizeRenderer::new);
		event.registerEntityRenderer(ErModEntities.HYDRO_CRYSTALLIZE.get(), HydroCrystallizeRenderer::new);
		event.registerEntityRenderer(ErModEntities.MIST_FLOWER.get(), MistFlowerRenderer::new);
		event.registerEntityRenderer(ErModEntities.FLAMING_FLOWER.get(), FlamingFlowerRenderer::new);
		event.registerEntityRenderer(ErModEntities.ANEMO_CRYSTALFLY.get(), AnemoCrystalflyRenderer::new);
		event.registerEntityRenderer(ErModEntities.TARTAGLIA.get(), TartagliaRenderer::new);
		event.registerEntityRenderer(ErModEntities.HILICHURL.get(), HilichurlRenderer::new);
		event.registerEntityRenderer(ErModEntities.ELECTRO_CICIN.get(), ElectroCicinRenderer::new);
		event.registerEntityRenderer(ErModEntities.ELEMENTAL_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(ErModEntities.FATUI_ELECTRO_CICIN_MAGE.get(), FatuiElectroCicinMageRenderer::new);
		event.registerEntityRenderer(ErModEntities.TRAVELER_TORNADO.get(), TravelerTornadoRenderer::new);
		event.registerEntityRenderer(ErModEntities.BLOSSOM_OF_WEALTH.get(), BlossomOfWealthRenderer::new);
		event.registerEntityRenderer(ErModEntities.TROUNCE_BLOSSOM.get(), TrounceBlossomRenderer::new);
		event.registerEntityRenderer(ErModEntities.BLOSSOM_OF_REVELATION.get(), BlossomOfRevelationRenderer::new);
		event.registerEntityRenderer(ErModEntities.BUTTERFLY.get(), ButterflyRenderer::new);
	}
}