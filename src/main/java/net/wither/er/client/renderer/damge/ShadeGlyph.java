package net.wither.er.client.renderer.damge;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

public interface ShadeGlyph {
    void er$render(int index, int total, boolean p_95227_, float x, float y, Matrix4f matrix4f, VertexConsumer p_95231_, float r, float g, float b, float a, int l);
}
