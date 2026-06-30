package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.er.entity.ButterflyEntity;
import net.mcreator.er.client.model.ModelCrystalfly;

public class ButterflyRenderer extends MobRenderer<ButterflyEntity, ModelCrystalfly<ButterflyEntity>> {
	private final ResourceLocation entityTexture = ResourceLocation.parse("er:textures/entities/butterfly.png");

	public ButterflyRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelCrystalfly<ButterflyEntity>(context.bakeLayer(ModelCrystalfly.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(ButterflyEntity entity) {
		return entityTexture;
	}
}