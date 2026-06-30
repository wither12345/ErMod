
package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.mcreator.er.entity.HilichurlEntity;
import net.mcreator.er.client.model.ModelErHumanoid;

public class HilichurlRenderer extends HumanoidMobRenderer<HilichurlEntity, ModelErHumanoid<HilichurlEntity>> {
	public HilichurlRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelErHumanoid(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
		this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(HilichurlEntity entity) {
        return new ResourceLocation("er:textures/entities/hilichurl.png");
	}
}
