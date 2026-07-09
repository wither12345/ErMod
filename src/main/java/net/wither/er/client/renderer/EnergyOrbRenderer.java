package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.wither.er.entity.EnergyOrb;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class EnergyOrbRenderer extends EntityRenderer<EnergyOrb> {
	private static final ResourceLocation ENERGY_ORB_LOCATION = new ResourceLocation("minecraft:textures/entity/experience_orb.png");
	private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(ENERGY_ORB_LOCATION);

	public EnergyOrbRenderer(EntityRendererProvider.Context p_174110_) {
		super(p_174110_);
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
	}

	private static void vertex(VertexConsumer p_254515_, Matrix4f p_253946_, Matrix3f p_253754_, float p_253952_, float p_254066_, int p_254283_, int p_254566_, int p_253882_, float p_254434_, float p_254223_, int p_254372_) {
		p_254515_
                .vertex(p_253946_, p_253952_, p_254066_, 0.0F)
                .color(p_254283_, p_254566_, p_253882_, 128)
                .uv(p_254434_, p_254223_)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(p_254372_).normal(p_253754_, 0.0F, 1.0F, 0.0F)
                .endVertex();
	}

	protected int getBlockLightLevel(@NotNull EnergyOrb orb, @NotNull BlockPos pos) {
		return Mth.clamp(super.getBlockLightLevel(orb, pos) + 7, 0, 15);
	}

	public void render(EnergyOrb orb, float p_114600_, float p_114601_, PoseStack stack, MultiBufferSource source, int p_114604_) {
		stack.pushPose();
		int i = (int) orb.getAmount();
		float f = (float) (i % 4 * 16) / 64.0F;
		float f1 = (float) (i % 4 * 16 + 16) / 64.0F;
		float f2 = (float) (i / 4 * 16) / 64.0F;
		float f3 = (float) (i / 4 * 16 + 16) / 64.0F;
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		float f7 = 255.0F;
		float f8 = ((float) orb.tickCount + p_114601_) / 2.0F;
		int j = getR(orb.getElement());
		int k = getG(orb.getElement());
		int l = getB(orb.getElement());
		stack.translate(0.0F, 0.1F, 0.0F);
		stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		float f9 = 0.3F;
		stack.scale(0.3F, 0.3F, 0.3F);
		VertexConsumer vertexconsumer = source.getBuffer(RENDER_TYPE);
		Matrix4f matrix4f = stack.last().pose();
		Matrix3f matrix3f = stack.last().normal();
		vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, -0.25F, j, 255, l, f, f3, p_114604_);
		vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, -0.25F, j, 255, l, f1, f3, p_114604_);
		vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, 0.75F, j, 255, l, f1, f2, p_114604_);
		vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, 0.75F, j, 255, l, f, f2, p_114604_);
		stack.popPose();
		super.render(orb, p_114600_, p_114601_, stack, source, p_114604_);
	}
	public @NotNull ResourceLocation getTextureLocation(@NotNull EnergyOrb orb) {
		return ENERGY_ORB_LOCATION;
	}

	private static int getR(int i) {
		if (i == 1)
			return 0;
		return 255;
	}

	private static int getG(int i) {
		if (i == 1)
			return 255;
		return 255;
	}

	private static int getB(int i) {
		if (i == 1)
			return 153;
		return 255;
	}
}