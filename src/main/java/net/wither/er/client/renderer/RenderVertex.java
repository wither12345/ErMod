package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class RenderVertex {
    public static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, int i, float v, float i1, float i2, float i3) {
        vertexConsumer.vertex(pose.pose(), v - 0.5F, i1 - 0.25F, 0.0F)
                .color(-1)
                .uv(i2, i3)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
    }
}
