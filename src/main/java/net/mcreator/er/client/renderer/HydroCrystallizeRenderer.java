package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.mcreator.er.entity.HydroCrystallizeEntity;
import net.mcreator.er.client.model.ModelCrystallize;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class HydroCrystallizeRenderer extends MobRenderer<HydroCrystallizeEntity, ModelCrystallize<HydroCrystallizeEntity>> {
	private final ResourceLocation entityTexture = new ResourceLocation("er:textures/entities/hydro_crystallize.png");

	public HydroCrystallizeRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelCrystallize<HydroCrystallizeEntity>(context.bakeLayer(ModelCrystallize.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<HydroCrystallizeEntity, ModelCrystallize<HydroCrystallizeEntity>>(this) {
			final ResourceLocation LAYER_TEXTURE = new ResourceLocation("er:textures/entities/hydro_crystallize.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, HydroCrystallizeEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE));
				this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0), 1, 1, 1, 1);
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(HydroCrystallizeEntity entity) {
		return entityTexture;
	}
}