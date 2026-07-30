package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.shield.ErShield;
import net.wither.er.shield.RenderShield;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = {Dist.CLIENT})
public class ErRenderers {
	private static final ResourceLocation pyro = new ResourceLocation("er:textures/mob_effect/pyro.png");

	@SubscribeEvent
	public static void onEntityRender(RenderLivingEvent.Post<?,?> event) {
		LivingEntity entity = event.getEntity();
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource bufferSource = event.getMultiBufferSource();
		LivingEntityRenderer<?,?> renderer = event.getRenderer();
		int light = event.getPackedLight();
		renderShield(entity, poseStack, bufferSource, renderer, light);
		renderLevel(entity, poseStack, bufferSource, renderer, light, event.getPartialTick());
		if(entity instanceof AuraContainerInterface auraContainerInterface)
			renderElements(auraContainerInterface.getElements(), entity, poseStack, bufferSource,light);
	}

	private static void renderShield(LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, LivingEntityRenderer<?,?> renderer, int light) {
		if (entity instanceof ErEntityInterface enti) {
			List<ErShield> shields = enti.er$getShields();
			for (ErShield shield : shields) {
				if (shield instanceof RenderShield rend) {
					poseStack.pushPose();
					poseStack.translate(0f, entity.getBbHeight() * 0, 0f);
					float scale = (float) Math.sqrt(Math.pow(entity.getBbHeight(), 2) * 2 + Math.pow(entity.getBbWidth(), 2));
					final Quaternionf cameraOrientation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();

					poseStack.scale(scale, scale, scale);
					poseStack.rotateAround(cameraOrientation, 0, 0.25f, 0);
					VertexConsumer vertexconsumer = bufferSource.getBuffer(rend.getRender());
					Matrix4f matrix4f = poseStack.last().pose();
					vertex(vertexconsumer, matrix4f, light, 0.0F, 0, 0, 1);
					vertex(vertexconsumer, matrix4f, light, 1.0F, 0, 1, 1);
					vertex(vertexconsumer, matrix4f, light, 1.0F, 1, 1, 0);
					vertex(vertexconsumer, matrix4f, light, 0.0F, 1, 0, 0);
					poseStack.popPose();
				}
			}
		}
	}
	

	private static void vertex(VertexConsumer consumer, Matrix4f matrix4f, int p_253829_, float p_253995_, int p_254031_, int p_253641_, int p_254243_) {
		consumer.vertex(matrix4f, p_253995_ - 0.5F, (float) p_254031_ - 0.25F, 0.0F)
                .color(-1)
                .uv((float) p_253641_, (float) p_254243_)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(p_253829_)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
	}

	private static void renderLevel(Entity entity, PoseStack posestack, MultiBufferSource bufferSource, LivingEntityRenderer<?,?> renderer, int packedLight, float partialTick) {
		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		double d0 = dispatcher.distanceToSqr(entity);
        Component level = Component.literal("level." + entity.getPersistentData().getInt("erLevel"));
        boolean flag = !entity.isDiscrete();
        float f = entity.getNameTagOffsetY();
        posestack.pushPose();
        posestack.translate(0.0F, f + 0.7, 0.0F);
        posestack.mulPose(dispatcher.cameraOrientation());
        posestack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = posestack.last().pose();
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int)(f1 * 255.0F) << 24;
        Font font = renderer.getFont();
        float f2 = (float)(-font.width(level) / 2);
        font.drawInBatch(level, f2, 0, 553648127, false, matrix4f, bufferSource, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, packedLight);
        if (flag) {
            font.drawInBatch(level, f2, 0, -1, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
        }

        posestack.popPose();
    }

	private static void renderElements(int elements ,Entity entity, PoseStack poseStack, MultiBufferSource bufferSource , int packedLight){
		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		int count = 0 ;
		int id = 1 ;
		for(int i = 0 ; i < Element.RenderId.values().length ; i ++){
			int value = (elements >> (i << 1)) & 3 ;
			if(value == 1 || value == 2){
				count ++ ;
			}
		}


		poseStack.pushPose();
		poseStack.translate(0, entity.getBbHeight() + 1, 0);
		poseStack.mulPose(dispatcher.cameraOrientation());
		//poseStack.rotateAround(dispatcher.cameraOrientation(), 0, 0, 0);

		int index = 0 ;
		for(Element.RenderId renderId : Element.RenderId.values()){
			int value = (elements & (3 << (index << 1))) >> (index << 1);
			if(value == 1 || value == 2){
				renderElement(renderId.getLocation(),value == 1 ,entity , poseStack ,bufferSource ,packedLight ,id ,count);
				id ++ ;
			}
			index ++ ;
		}
		poseStack.popPose();
	}

	private static void renderElement(ResourceLocation texture , boolean gauge , Entity entity, PoseStack poseStack, MultiBufferSource bufferSource , int packedLight , int id , int total){
		Minecraft.getInstance().getTextureManager().bindForSetup(texture);
		VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));

		poseStack.pushPose();
		poseStack.scale(0.4F, 0.4F, 0.4F);
		poseStack.translate(id - (total + 1)/2f, 0, 0);

		Matrix4f matrix = poseStack.last().pose();

		float alpha = 1.0f;
		if (gauge) {
			float time = Minecraft.getInstance().level.getGameTime() %  1000 / 1000f;
			alpha = 0.6f + 0.2f * Mth.sin(time * (float)Math.PI * 2);
		}

		int color = (int)(alpha * 255) * (0x010101);

		vertexConsumer.vertex(matrix, -0.5f, -0.5f, 0)
				.color(color)
				.uv(0,1)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(packedLight)
				.normal( 0, 1, 0)
				.endVertex();

		vertexConsumer.vertex(matrix, 0.5f, -0.5f, 0)
				.color(color)
				.uv(1,1)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(packedLight)
				.normal( 0, 1, 0)
				.endVertex();

		vertexConsumer.vertex(matrix, 0.5f, 0.5f, 0)
				.color(color)
				.uv(1,0)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(packedLight)
				.normal( 0, 1, 0)
				.endVertex();

		vertexConsumer.vertex(matrix, -0.5f, 0.5f, 0)
				.color(color)
				.uv(0,0)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(packedLight)
				.normal( 0, 1, 0)
				.endVertex();
		poseStack.popPose();
	}
}