package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class RenderVertex {
    public static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, int i, float v, int i1, float i2, float i3) {
        vertexConsumer.addVertex(pose, v - 0.5F, (float)i1 - 0.25F, 0.0F)
                .setColor(-1)
                .setUv(i2, i3)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(i)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
