package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.Hyperbloom;

public class HyperbloomRenderer extends EntityRenderer<Hyperbloom> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("er:textures/entities/hyperbloom.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);

    public HyperbloomRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected int getBlockLightLevel(Hyperbloom p_114087_, BlockPos p_114088_) {
        return 15;
    }

    public void render(Hyperbloom p_114080_, float p_114081_, float p_114082_, PoseStack p_114083_, MultiBufferSource p_114084_, int p_114085_) {
        p_114083_.pushPose();
        p_114083_.scale(0.4F, 0.4F, 0.4F);
        p_114083_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        PoseStack.Pose posestack$pose = p_114083_.last();
        VertexConsumer vertexconsumer = p_114084_.getBuffer(RENDER_TYPE);
        RenderVertex.vertex(vertexconsumer, posestack$pose, p_114085_, 0.0F, 0, 0, 1);
        RenderVertex.vertex(vertexconsumer, posestack$pose, p_114085_, 1.0F, 0, 1, 1);
        RenderVertex.vertex(vertexconsumer, posestack$pose, p_114085_, 1.0F, 1, 1, 0);
        RenderVertex.vertex(vertexconsumer, posestack$pose, p_114085_, 0.0F, 1, 0, 0);
        p_114083_.popPose();
        super.render(p_114080_, p_114081_, p_114082_, p_114083_, p_114084_, p_114085_);
    }

    public ResourceLocation getTextureLocation(Hyperbloom p_114078_) {
        return TEXTURE_LOCATION;
    }
}
