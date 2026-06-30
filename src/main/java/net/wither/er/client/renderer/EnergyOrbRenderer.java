package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.wither.er.entity.EnergyOrb;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(value = {Dist.CLIENT})
@OnlyIn(Dist.CLIENT)
public class EnergyOrbRenderer extends EntityRenderer<EnergyOrb> {
	private static final ResourceLocation ENERGY_ORB_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/experience_orb.png");
	private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(ENERGY_ORB_LOCATION);

	public EnergyOrbRenderer(EntityRendererProvider.Context p_174110_) {
		super(p_174110_);
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
	}

	private static void vertex(VertexConsumer p_254515_, PoseStack.Pose p_324046_, float p_253952_, float p_254066_, int p_254283_, int p_254566_, int p_253882_, float p_254434_, float p_254223_, int p_254372_) {
		p_254515_.addVertex(p_324046_, p_253952_, p_254066_, 0.0F).setColor(p_254283_, p_254566_, p_253882_, 128).setUv(p_254434_, p_254223_).setOverlay(OverlayTexture.NO_OVERLAY).setLight(p_254372_).setNormal(p_324046_, 0.0F, 1.0F, 0.0F);
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
		PoseStack.Pose posestack$pose = stack.last();
		vertex(vertexconsumer, posestack$pose, -0.5F, -0.25F, j, k, l, f, f3, p_114604_);
		vertex(vertexconsumer, posestack$pose, 0.5F, -0.25F, j, k, l, f1, f3, p_114604_);
		vertex(vertexconsumer, posestack$pose, 0.5F, 0.75F, j, k, l, f1, f2, p_114604_);
		vertex(vertexconsumer, posestack$pose, -0.5F, 0.75F, j, k, l, f, f2, p_114604_);
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

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ErModEntities.ENERGY_ORB.get(), EnergyOrbRenderer::new);
	}
}