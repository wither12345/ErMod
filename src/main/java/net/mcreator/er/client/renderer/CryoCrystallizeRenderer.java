package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.mcreator.er.entity.CryoCrystallizeEntity;
import net.mcreator.er.client.model.ModelCrystallize;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class CryoCrystallizeRenderer extends MobRenderer<CryoCrystallizeEntity, ModelCrystallize<CryoCrystallizeEntity>> {
	private final ResourceLocation entityTexture = ResourceLocation.parse("er:textures/entities/cryo_crystallize.png");

	public CryoCrystallizeRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelCrystallize<CryoCrystallizeEntity>(context.bakeLayer(ModelCrystallize.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<CryoCrystallizeEntity, ModelCrystallize<CryoCrystallizeEntity>>(this) {
			final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("er:textures/entities/cryo_crystallize.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, CryoCrystallizeEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE));
				this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(CryoCrystallizeEntity entity) {
		return entityTexture;
	}
}