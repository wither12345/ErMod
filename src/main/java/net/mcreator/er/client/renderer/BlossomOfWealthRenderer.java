package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;

import net.mcreator.er.entity.BlossomOfWealthEntity;
import net.mcreator.er.client.model.ModelLey_Line_Outcrop;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class BlossomOfWealthRenderer extends MobRenderer<BlossomOfWealthEntity, ModelLey_Line_Outcrop<BlossomOfWealthEntity>> {
	public BlossomOfWealthRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelLey_Line_Outcrop<BlossomOfWealthEntity>(context.bakeLayer(ModelLey_Line_Outcrop.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(BlossomOfWealthEntity entity) {
        return new ResourceLocation("er:textures/entities/blossom_of_wealth.png");
	}

	@Override
	public void render(BlossomOfWealthEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		float alpha = 0.5F;
		poseStack.pushPose();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}
}