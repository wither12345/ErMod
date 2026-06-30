package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.mcreator.er.entity.AnemoCrystalflyEntity;
import net.mcreator.er.client.model.ModelCrystalfly;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class AnemoCrystalflyRenderer extends MobRenderer<AnemoCrystalflyEntity, ModelCrystalfly<AnemoCrystalflyEntity>> {
	private final ResourceLocation entityTexture = new ResourceLocation("er:textures/entities/anemo_crystalfly.png");

	public AnemoCrystalflyRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelCrystalfly<AnemoCrystalflyEntity>(context.bakeLayer(ModelCrystalfly.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<AnemoCrystalflyEntity, ModelCrystalfly<AnemoCrystalflyEntity>>(this) {
			final ResourceLocation LAYER_TEXTURE = new ResourceLocation("er:textures/entities/anemo_crystalfly.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, AnemoCrystalflyEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE));
				this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0), 1, 1, 1, 1);
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(AnemoCrystalflyEntity entity) {
		return entityTexture;
	}
}