/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.mcreator.er.client.model.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ErModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelAnemoSlime.LAYER_LOCATION, ModelAnemoSlime::createBodyLayer);
		event.registerLayerDefinition(Modelbloom.LAYER_LOCATION, Modelbloom::createBodyLayer);
		event.registerLayerDefinition(ModelSlime.LAYER_LOCATION, ModelSlime::createBodyLayer);
		event.registerLayerDefinition(ModelCrystallize.LAYER_LOCATION, ModelCrystallize::createBodyLayer);
		event.registerLayerDefinition(ModelLey_Line_Outcrop.LAYER_LOCATION, ModelLey_Line_Outcrop::createBodyLayer);
		event.registerLayerDefinition(ModelElectroSlime.LAYER_LOCATION, ModelElectroSlime::createBodyLayer);
		event.registerLayerDefinition(ModelTrounceBlossom.LAYER_LOCATION, ModelTrounceBlossom::createBodyLayer);
		event.registerLayerDefinition(ModelDendroSlime.LAYER_LOCATION, ModelDendroSlime::createBodyLayer);
		event.registerLayerDefinition(ModelCrystalfly.LAYER_LOCATION, ModelCrystalfly::createBodyLayer);
		event.registerLayerDefinition(Modelelectro_cicin.LAYER_LOCATION, Modelelectro_cicin::createBodyLayer);
		event.registerLayerDefinition(ModelWhopperflower.LAYER_LOCATION, ModelWhopperflower::createBodyLayer);
		event.registerLayerDefinition(ModelFatuiElectroCicinMage.LAYER_LOCATION, ModelFatuiElectroCicinMage::createBodyLayer);
		event.registerLayerDefinition(ModelTornado.LAYER_LOCATION, ModelTornado::createBodyLayer);
		event.registerLayerDefinition(ModelFlaming_Flower.LAYER_LOCATION, ModelFlaming_Flower::createBodyLayer);
		event.registerLayerDefinition(Modellarge_hydro_slime.LAYER_LOCATION, Modellarge_hydro_slime::createBodyLayer);
		event.registerLayerDefinition(ModelMask.LAYER_LOCATION, ModelMask::createBodyLayer);
		event.registerLayerDefinition(ModelMist_Flower.LAYER_LOCATION, ModelMist_Flower::createBodyLayer);
		event.registerLayerDefinition(ModelErHumanoid.LAYER_LOCATION, ModelErHumanoid::createBodyLayer);
		event.registerLayerDefinition(ModelGeoSlime.LAYER_LOCATION, ModelGeoSlime::createBodyLayer);
		event.registerLayerDefinition(Modellarge_electro_slime.LAYER_LOCATION, Modellarge_electro_slime::createBodyLayer);
	}
}