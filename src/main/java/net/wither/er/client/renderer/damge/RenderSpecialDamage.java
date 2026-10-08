package net.wither.er.client.renderer.damge;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.er.ERClientConfig;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

public class RenderSpecialDamage {
    public static void renderLunarText(MultiBufferSource source, Camera camera, String string, double x, double y, double z, int color, float scaling, RenderDamageAmount.DamageDisplayType type) {
        Minecraft minecraft = Minecraft.getInstance();
        if (camera.isInitialized()) {
            PoseStack pose = new PoseStack();
            Font font = minecraft.font;
            double d0 = camera.getPosition().x;
            double d1 = camera.getPosition().y;
            double d2 = camera.getPosition().z;
            pose.pushPose();
            pose.translate((float) (x - d0), (float) (y - d1) + 0.07F, (float) (z - d2));
            pose.mulPose(camera.rotation());
            pose.scale(scaling, -scaling, scaling);
            float f = (float) (-font.width(string)) / 2.0F;
            drawInternal(font, string, f, color, pose.last().pose(), source, font.isBidirectional(), type);
            pose.popPose();
        }
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

    private static void renderChar(IShadeGlyph bakedGlyph, boolean italic, float x, float y, Matrix4f matrix4f, VertexConsumer vertexConsumer, float r, float g, float b, float a, int packedLightCoords, RenderDamageAmount.DamageDisplayType type, boolean shadow) {
        int count = ERClientConfig.DAMAGE_CUTTING.get();
        if(shadow){
            bakedGlyph.er$render(0, 1, italic, x, y, matrix4f, vertexConsumer,
                    shadow(r), shadow(g), shadow(b), a, packedLightCoords);
            return;
        }

        switch (type){
            case LUNAR -> {
                for(int i = 0 ; i < count ; i ++)
                    bakedGlyph.er$render(i, count, italic, x, y, matrix4f, vertexConsumer,
                            mix(i, count, r),
                            mix(i, count, g),
                            mix(i, count, b),
                            a, packedLightCoords);
            }
            case STELLAR -> {
                for(int i = 0 ; i < count ; i ++)
                    bakedGlyph.er$render(i, count, italic, x, y, matrix4f, vertexConsumer,
                            mix(count - i, count, r),
                            mix(count - i, count, g),
                            mix(count - i, count, b),
                            a, packedLightCoords);
            }
            case NORMAL -> bakedGlyph.er$render(0, 1, italic, x, y, matrix4f, vertexConsumer, r, g, b, a, packedLightCoords);
        }
    }

    private static float mix(int index, int total, float ori){
        return (ori * (total - index) + index)/total;
    }

    private static float shadow(float ori){
        return ori / 4;
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
        private final Font.DisplayMode mode = Font.DisplayMode.NORMAL;
        private final RenderDamageAmount.DamageDisplayType type;
        float x;
        float y;

        public StringRenderOutput(MultiBufferSource bufferSource, Font font, float x, float y, int argb, boolean dropShadow, Matrix4f matrix4f, int packedLightCoords, RenderDamageAmount.DamageDisplayType type) {
            this.bufferSource = bufferSource;
            this.font = font;
            this.x = x;
            this.y = y;
            this.dropShadow = dropShadow;
            this.dimFactor = dropShadow ? 0.25F : 1.0F;
            this.r = (float)(argb >> 16 & 255) / 255.0F * this.dimFactor;
            this.g = (float)(argb >> 8 & 255) / 255.0F * this.dimFactor;
            this.b = (float)(argb & 255) / 255.0F * this.dimFactor;
            this.a = (float)(argb >> 24 & 255) / 255.0F;
            this.pose = matrix4f;
            this.packedLightCoords = packedLightCoords;
            this.type = type;
        }

        @Override
        public boolean accept(int p_92967_, Style style, int p_92969_) {
            FontSet fontset = font.getFontSet(style.getFont());
            GlyphInfo glyphinfo = fontset.getGlyphInfo(p_92969_, font.filterFishyGlyphs);
            BakedGlyph bakedglyph = style.isObfuscated() && p_92969_ != 32 ? fontset.getRandomGlyph(glyphinfo) : fontset.getGlyph(p_92969_);
            boolean flag = style.isBold();
            TextColor textcolor = style.getColor();
            float r;
            float g;
            float b;
            if (textcolor != null) {
                int i = textcolor.getValue();
                r = (float)(i >> 16 & 255) / 255.0F * this.dimFactor;
                g = (float)(i >> 8 & 255) / 255.0F * this.dimFactor;
                b = (float)(i & 255) / 255.0F * this.dimFactor;
            } else {
                r = this.r;
                g = this.g;
                b = this.b;
            }

            if (!(bakedglyph instanceof EmptyGlyph) && bakedglyph instanceof IShadeGlyph shadeGlyph) {
                float f4 = this.dropShadow ? glyphinfo.getShadowOffset() : 0.0F;
                VertexConsumer vertexconsumer = this.bufferSource.getBuffer(bakedglyph.renderType(this.mode));
                Matrix4f bg = new Matrix4f(this.pose).translate(0, 0, -0.01f);
                renderChar(shadeGlyph, style.isItalic(), this.x + f4, this.y + f4, this.pose, vertexconsumer, r, g, b, this.a, this.packedLightCoords, this.type, false);
                renderChar(shadeGlyph, style.isItalic(), this.x + f4 + 1, this.y + f4 + 1, bg, vertexconsumer, r, g, b, this.a, this.packedLightCoords, this.type, true);
            }

            float f6 = glyphinfo.getAdvance(flag);

            this.x += f6;
            return true;
        }
    }
}