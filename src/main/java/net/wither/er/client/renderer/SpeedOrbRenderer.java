package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.SpeedOrbEntity;
import org.jetbrains.annotations.NotNull;

public class SpeedOrbRenderer extends EntityRenderer<SpeedOrbEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("er:textures/entities/speed_orb.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);

    public SpeedOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected int getBlockLightLevel(@NotNull SpeedOrbEntity projectile, @NotNull BlockPos pos) {
        return 15;
    }

    public void render(@NotNull SpeedOrbEntity projectile, float v, float v1, PoseStack poseStack, MultiBufferSource source, int i) {
        poseStack.pushPose();
        poseStack.scale(0.6F, 0.6F, 0.6F);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        PoseStack.Pose posestack$pose = poseStack.last();
        VertexConsumer vertexconsumer = source.getBuffer(RENDER_TYPE);
        RenderVertex.vertex(vertexconsumer, posestack$pose, i, 0.0F, 0, 0, 1);
        RenderVertex.vertex(vertexconsumer, posestack$pose, i, 1.0F, 0, 1, 1);
        RenderVertex.vertex(vertexconsumer, posestack$pose, i, 1.0F, 1, 1, 0);
        RenderVertex.vertex(vertexconsumer, posestack$pose, i, 0.0F, 1, 0, 0);
        poseStack.popPose();
        super.render(projectile, v, v1, poseStack, source, i);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull SpeedOrbEntity projectile) {
        return TEXTURE_LOCATION;
    }
}
