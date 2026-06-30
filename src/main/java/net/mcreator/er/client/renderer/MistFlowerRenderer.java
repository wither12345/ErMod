package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.er.entity.MistFlowerEntity;
import net.mcreator.er.client.model.ModelMist_Flower;

public class MistFlowerRenderer extends MobRenderer<MistFlowerEntity, ModelMist_Flower<MistFlowerEntity>> {
	private final ResourceLocation entityTexture = ResourceLocation.parse("er:textures/entities/mist_flower.png");

	public MistFlowerRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelMist_Flower<MistFlowerEntity>(context.bakeLayer(ModelMist_Flower.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(MistFlowerEntity entity) {
		return entityTexture;
	}
}