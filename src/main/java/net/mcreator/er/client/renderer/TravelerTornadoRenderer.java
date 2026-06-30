package net.mcreator.er.client.renderer;

import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.mcreator.er.entity.TravelerTornadoEntity;
import net.mcreator.er.client.model.ModelTornado;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class TravelerTornadoRenderer extends MobRenderer<TravelerTornadoEntity, ModelTornado<TravelerTornadoEntity>> {
	public TravelerTornadoRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelTornado<TravelerTornadoEntity>(context.bakeLayer(ModelTornado.LAYER_LOCATION)), 1f);
		this.addLayer(new RenderLayer<TravelerTornadoEntity, ModelTornado<TravelerTornadoEntity>>(this) {
			final ResourceLocation ANEMO = ResourceLocation.parse("er:textures/entities/tornado.png");
			final ResourceLocation ELECTRO = ResourceLocation.parse("er:textures/entities/electro_tornado.png");
			final ResourceLocation HYDRO = ResourceLocation.parse("er:textures/entities/hydro_tornado.png");
			final ResourceLocation PYRO = ResourceLocation.parse("er:textures/entities/pyro_tornado.png");
			final ResourceLocation CRYO = ResourceLocation.parse("er:textures/entities/cryo_tornado.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, TravelerTornadoEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				Level world = entity.level();
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				light = 255;
				if (entity.getEntityData().get(TravelerTornadoEntity.DATA_Absorption) == 1) {
					VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(ELECTRO));
					this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
				} else if (entity.getEntityData().get(TravelerTornadoEntity.DATA_Absorption) == 2) {
					VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(HYDRO));
					this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
				} else if (entity.getEntityData().get(TravelerTornadoEntity.DATA_Absorption) == 3) {
					VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(PYRO));
					this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
				} else if (entity.getEntityData().get(TravelerTornadoEntity.DATA_Absorption) == 4) {
					VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(CRYO));
					this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
				} else {
					VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(ANEMO));
					this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
				}
			}
		});
	}

	@Override
	protected void scale(TravelerTornadoEntity entity, PoseStack poseStack, float f) {
		float scaling = Math.min(entity.getAliveTick() * 0.2f, 1);
		poseStack.scale(scaling, scaling, scaling);
	}

	@Override
	public ResourceLocation getTextureLocation(TravelerTornadoEntity entity) {
		return ResourceLocation.parse("er:textures/entities/null.png");
	}
}