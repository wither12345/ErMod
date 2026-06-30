
package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.er.entity.FatuiElectroCicinMageEntity;
import net.mcreator.er.client.model.ModelFatuiElectroCicinMage;

public class FatuiElectroCicinMageRenderer extends HumanoidMobRenderer<FatuiElectroCicinMageEntity, ModelFatuiElectroCicinMage<FatuiElectroCicinMageEntity>> {
	public FatuiElectroCicinMageRenderer(EntityRendererProvider.Context context) {
		//super(context, new ModelFatuiElectroCicinMage(context.bakeLayer(ModelLayers.PLAYER_SLIM)), 0.5f);
		super(context, new ModelFatuiElectroCicinMage<FatuiElectroCicinMageEntity>(context.bakeLayer(ModelFatuiElectroCicinMage.LAYER_LOCATION)), 0.5f);
		//this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(FatuiElectroCicinMageEntity entity) {
		return ResourceLocation.parse("er:textures/entities/fatui_electro_cicin_mage.png");
	}
}
 