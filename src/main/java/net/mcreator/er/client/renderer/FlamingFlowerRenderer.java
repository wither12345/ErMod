package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.er.entity.FlamingFlowerEntity;
import net.mcreator.er.client.model.ModelFlaming_Flower;

public class FlamingFlowerRenderer extends MobRenderer<FlamingFlowerEntity, ModelFlaming_Flower<FlamingFlowerEntity>> {
	private final ResourceLocation entityTexture = ResourceLocation.parse("er:textures/entities/flaming_flower.png");

	public FlamingFlowerRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelFlaming_Flower<FlamingFlowerEntity>(context.bakeLayer(ModelFlaming_Flower.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(FlamingFlowerEntity entity) {
		return entityTexture;
	}
}