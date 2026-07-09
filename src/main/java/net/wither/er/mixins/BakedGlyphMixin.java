package net.wither.er.mixins;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.wither.er.client.renderer.damge.ShadeGlyph;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BakedGlyph.class)
public class BakedGlyphMixin implements ShadeGlyph {
    @Shadow @Final private float u0;
    @Shadow @Final private float u1;
    @Shadow @Final private float v0;
    @Shadow @Final private float v1;
    @Shadow @Final private float left;
    @Shadow @Final private float right;
    @Shadow @Final private float up;
    @Shadow @Final private float down;

    @Override
    public void er$render(int index, int total, boolean p_95227_, float x, float y, Matrix4f matrix4f, VertexConsumer p_95231_, float r, float g, float b, float a, int l) {
        // 1. 计算显示比例：每份占完整纹理的 1/total
        float ratio = 1.0f / total;

        // 2. 计算当前份在纹理上的纵向范围 (v0, v1)
        float step = (this.v1 - this.v0) / total;
        float currentV0 = this.v0 + index * step;
        float currentV1 = this.v0 + (index + 1) * step;

        // 3. 计算几何位置的高度缩放和偏移
        //    原始高度 = this.down - this.up (注意 up/down 可能是负数或正数)
        float height = this.down - this.up;
        // 当前份的顶部和底部相对于原始 up 的偏移量 (比例)
        float topOffset = index * ratio * height;
        float bottomOffset = (index + 1) * ratio * height;

        // 计算新的 up 和 down (相对于原始 up 的偏移)
        float newUp = this.up + topOffset;
        float newDown = this.up + bottomOffset;

        // 4. 用新的 up/down 计算顶点位置
        float f = x + this.left;
        float f1 = x + this.right;
        float f2 = y + newUp;
        float f3 = y + newDown;
        // 如果存在倾斜效果 (p_95227_)，仍按原逻辑计算偏移量 (但最好也按比例调整)
        float f4 = p_95227_ ? 1.0F - 0.25F * newUp : 0.0F;
        float f5 = p_95227_ ? 1.0F - 0.25F * newDown : 0.0F;

        // 5. 使用新的纹理坐标和顶点位置绘制
        p_95231_.addVertex(matrix4f, f + f4, f2, 0.0F)
                .setColor(r, g, b, a)
                .setUv(this.u0, currentV0)   // 左下角
                .setLight(l);
        p_95231_.addVertex(matrix4f, f + f5, f3, 0.0F)
                .setColor(r, g, b, a)
                .setUv(this.u0, currentV1)   // 左上角
                .setLight(l);
        p_95231_.addVertex(matrix4f, f1 + f5, f3, 0.0F)
                .setColor(r, g, b, a)
                .setUv(this.u1, currentV1)   // 右上角
                .setLight(l);
        p_95231_.addVertex(matrix4f, f1 + f4, f2, 0.0F)
                .setColor(r, g, b, a)
                .setUv(this.u1, currentV0)   // 右下角
                .setLight(l);
    }
}
