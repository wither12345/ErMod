package net.wither.er.client.renderer.damage;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

public class RenderSpecialDamage {
    public static void renderLunarText(PoseStack poseStack, MultiBufferSource bufferSource, String string, double x, double y, double z, int color, float scaling, RenderDamageAmount.DamageDisplayType type) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        if (camera.isInitialized()) {
            Font font = minecraft.font;
            double d0 = camera.getPosition().x;
            double d1 = camera.getPosition().y;
            double d2 = camera.getPosition().z;
            poseStack.pushPose();
            poseStack.translate((float) (x - d0), (float) (y - d1), (float) (z - d2));
            poseStack.mulPoseMatrix((new Matrix4f()).rotation(camera.rotation()));
            poseStack.scale(-scaling, -scaling, -scaling);
            float f =(float) (-font.width(string)) / 2.0F ;
            drawInBatch(font, string, f, color, poseStack.last().pose(),  bufferSource, font.isBidirectional(), type);
            poseStack.popPose();
        }
    }

    private static void drawInBatch(Font font, String s, float f, int color, Matrix4f matrix4f, MultiBufferSource multiBufferSource, boolean flag, RenderDamageAmount.DamageDisplayType type) {
        drawInternal(font, s, f, color, matrix4f, multiBufferSource, flag, type);
    }

    private static int adjustColor(int p_92720_) {
        return (p_92720_ & -67108864) == 0 ? p_92720_ | -16777216 : p_92720_;
    }

    private static void drawInternal(Font font, String string, float f, int color, Matrix4f matrix4f1, MultiBufferSource multiBufferSource, boolean flag, RenderDamageAmount.DamageDisplayType type) {
        if (flag) {
            string = font.bidirectionalShaping(string);
        }

        color = adjustColor(color);
        Matrix4f matrix4f = new Matrix4f(matrix4f1);

        renderText(font, string, f, color, matrix4f, multiBufferSource, type);
    }

    private static void renderText(Font font, String p_273765_, float f, int color, Matrix4f matrix4f, MultiBufferSource multiBufferSource, RenderDamageAmount.DamageDisplayType type) {
        StringRenderOutput font$stringrenderoutput = new StringRenderOutput(multiBufferSource, font, f, (float) 0.0, color, false, matrix4f, 15728880, type);
        StringDecomposer.iterateFormatted(p_273765_, Style.EMPTY, font$stringrenderoutput);
    }

    private static void renderChar(ShadeGlyph bakedGlyph, boolean p_254262_, float x, float y, Matrix4f matrix4f, VertexConsumer vertexConsumer, float r, float g, float b, float a, int p_253905_, RenderDamageAmount.DamageDisplayType type) {
        switch (type){
            case LUNAR -> {
                for(int i = 0 ; i < 8 ; i ++)
                    bakedGlyph.er$render(i, 8, p_254262_, x, y, matrix4f, vertexConsumer,
                            mix(i, 8, r),
                            mix(i, 8, g),
                            mix(i, 8, b),
                            a, p_253905_);
            }
            case STELLAR -> {
                for(int i = 0 ; i < 8 ; i ++)
                    bakedGlyph.er$render(i, 8, p_254262_, x, y, matrix4f, vertexConsumer,
                            mix(8 - i, 8, r),
                            mix(8 - i, 8, g),
                            mix(8 - i, 8, b),
                            a, p_253905_);
            }
            case NORMAL -> bakedGlyph.er$render(0, 1, p_254262_, x, y, matrix4f, vertexConsumer, r, g, b, a, p_253905_);
        }
    }

    private static float mix(int index, int total, float ori){
        return (ori * (total - index) + index)/total;
    }

    @OnlyIn(Dist.CLIENT)
    public static class StringRenderOutput implements FormattedCharSink {
        final MultiBufferSource bufferSource;
        private final Font font;
        private final boolean dropShadow;
        private final float dimFactor;
        private final float r;
        private final float g;
        private final float b;
        private final float a;
        private final Matrix4f pose;
        private final int packedLightCoords;
        private final Font.DisplayMode mode = Font.DisplayMode.SEE_THROUGH;
        private final RenderDamageAmount.DamageDisplayType type;
        float x;
        float y;

        public StringRenderOutput(MultiBufferSource p_181365_, Font font, float p_181366_, float p_181367_, int p_181368_, boolean p_181369_, Matrix4f p_254510_, int p_181372_, RenderDamageAmount.DamageDisplayType type) {
            this.bufferSource = p_181365_;
            this.font = font;
            this.x = p_181366_;
            this.y = p_181367_;
            this.dropShadow = p_181369_;
            this.dimFactor = p_181369_ ? 0.25F : 1.0F;
            this.r = (float)(p_181368_ >> 16 & 255) / 255.0F * this.dimFactor;
            this.g = (float)(p_181368_ >> 8 & 255) / 255.0F * this.dimFactor;
            this.b = (float)(p_181368_ & 255) / 255.0F * this.dimFactor;
            this.a = (float)(p_181368_ >> 24 & 255) / 255.0F;
            this.pose = p_254510_;
            this.packedLightCoords = p_181372_;
            this.type = type;
        }

        public boolean accept(int p_92967_, Style style, int p_92969_) {
            FontSet fontset = ((FontAccessor)font).er$callGetFontSet(style.getFont());
            GlyphInfo glyphinfo = fontset.getGlyphInfo(p_92969_,  ((FontAccessor)font).er$getFilterFishyGlyphs());
            BakedGlyph bakedglyph = style.isObfuscated() && p_92969_ != 32 ? fontset.getRandomGlyph(glyphinfo) : fontset.getGlyph(p_92969_);
            boolean flag = style.isBold();
            TextColor textcolor = style.getColor();
            float f;
            float f1;
            float f2;
            if (textcolor != null) {
                int i = textcolor.getValue();
                f = (float)(i >> 16 & 255) / 255.0F * this.dimFactor;
                f1 = (float)(i >> 8 & 255) / 255.0F * this.dimFactor;
                f2 = (float)(i & 255) / 255.0F * this.dimFactor;
            } else {
                f = this.r;
                f1 = this.g;
                f2 = this.b;
            }

            if (!(bakedglyph instanceof EmptyGlyph) && bakedglyph instanceof ShadeGlyph shadeGlyph) {
                float f4 = this.dropShadow ? glyphinfo.getShadowOffset() : 0.0F;
                VertexConsumer vertexconsumer = this.bufferSource.getBuffer(bakedglyph.renderType(this.mode));
                renderChar(shadeGlyph, style.isItalic(), this.x + f4, this.y + f4, this.pose, vertexconsumer, f, f1, f2, this.a, this.packedLightCoords, this.type);
            }

            float f6 = glyphinfo.getAdvance(flag);

            this.x += f6;
            return true;
        }
    }
}