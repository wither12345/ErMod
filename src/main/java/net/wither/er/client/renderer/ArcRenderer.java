
package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.wither.er.entity.ArcEntity;

public class ArcRenderer extends EntityRenderer<ArcEntity> {
	private static final ResourceLocation BEAM_LOCATION = ResourceLocation.parse("er:textures/entities/arc.png");
	private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(BEAM_LOCATION);

	public ArcRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	private Vec3 getPosition(Entity p_114803_, double p_114804_, float p_114805_) {
		double d0 = Mth.lerp((double) p_114805_, p_114803_.xOld, p_114803_.getX());
		double d1 = Mth.lerp((double) p_114805_, p_114803_.yOld, p_114803_.getY()) + p_114804_;
		double d2 = Mth.lerp((double) p_114805_, p_114803_.zOld, p_114803_.getZ());
		return new Vec3(d0, d1, d2);
	}

	public boolean shouldRender(ArcEntity p_114836_, Frustum p_114837_, double p_114838_, double p_114839_, double p_114840_) {
		if (super.shouldRender(p_114836_, p_114837_, p_114838_, p_114839_, p_114840_)) {
			return true;
		} else {
			if (p_114836_.hasActiveTarget()) {
				return true;
			}
			return false;
		}
	}

	public void render(ArcEntity arc, float p_114830_, float p_114831_, PoseStack p_114832_, MultiBufferSource p_114833_, int p_114834_) {
		super.render(arc, p_114830_, p_114831_, p_114832_, p_114833_, p_114834_);
		Entity livingentity = arc.getTarget();
		if (livingentity != null) {
			float f = arc.getRestScale(p_114831_);
			float f1 = arc.getRest() + p_114831_;
			float f2 = f1 * 0.5F % 1.0F;
			float f3 = 0;
			p_114832_.pushPose();
			p_114832_.translate(0.0F, f3, 0.0F);
			Vec3 vec3 = this.getPosition(livingentity, (double) livingentity.getBbHeight() * 0.5D, p_114831_);
			Vec3 vec31 = this.getPosition(arc, (double) f3, p_114831_);
			Vec3 vec32 = vec3.subtract(vec31);
			float f4 = (float) (vec32.length() + 1.0D);
			vec32 = vec32.normalize();
			float f5 = (float) Math.acos(vec32.y);
			float f6 = (float) Math.atan2(vec32.z, vec32.x);
			p_114832_.mulPose(Axis.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
			p_114832_.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));
			float f7 = f1 * 0.05F * -1.5F;
			int j = 255;
			int k = 255;
			int l = 255;
			float f19 = Mth.cos(f7 + (float) Math.PI) * 0.2F;
			float f20 = Mth.sin(f7 + (float) Math.PI) * 0.2F;
			float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
			float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
			float f23 = Mth.cos(f7 + ((float) Math.PI / 2F)) * 0.2F;
			float f24 = Mth.sin(f7 + ((float) Math.PI / 2F)) * 0.2F;
			float f25 = Mth.cos(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
			float f26 = Mth.sin(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
			float f29 = -1.0F + f2;
			float f30 = f4 * 2.5F + f29;
			VertexConsumer vertexconsumer = p_114833_.getBuffer(BEAM_RENDER_TYPE);
			PoseStack.Pose posestack$pose = p_114832_.last();
			vertex(vertexconsumer, posestack$pose, f19, f4, f20, j, k, l, 0.4999F, f30);
			vertex(vertexconsumer, posestack$pose, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
			vertex(vertexconsumer, posestack$pose, f21, 0.0F, f22, j, k, l, 0.0F, f29);
			vertex(vertexconsumer, posestack$pose, f21, f4, f22, j, k, l, 0.0F, f30);
			vertex(vertexconsumer, posestack$pose, f23, f4, f24, j, k, l, 0.4999F, f30);
			vertex(vertexconsumer, posestack$pose, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
			vertex(vertexconsumer, posestack$pose, f25, 0.0F, f26, j, k, l, 0.0F, f29);
			vertex(vertexconsumer, posestack$pose, f25, f4, f26, j, k, l, 0.0F, f30);
			//vertex(vertexconsumer, posestack$pose, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
			//vertex(vertexconsumer, posestack$pose, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
			//vertex(vertexconsumer, posestack$pose, f17, f4, f18, j, k, l, 1.0F, f31);
			//vertex(vertexconsumer, posestack$pose, f15, f4, f16, j, k, l, 0.5F, f31);
			p_114832_.popPose();
		}
	}

	private static void vertex(VertexConsumer p_253637_, PoseStack.Pose p_323627_, float p_253994_, float p_254492_, float p_254474_, int p_254080_, int p_253655_, int p_254133_, float p_254233_, float p_253939_) {
		p_253637_.addVertex(p_323627_, p_253994_, p_254492_, p_254474_).setColor(p_254080_, p_253655_, p_254133_, 255).setUv(p_254233_, p_253939_).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(p_323627_, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(ArcEntity entity) {
		return ResourceLocation.parse("textures/entity/guardian_beam.png");
	}
}
