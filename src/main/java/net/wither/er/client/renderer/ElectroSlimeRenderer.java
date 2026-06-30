package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.er.client.model.ModelElectroSlime;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.entity.slimes.ElectroSlime;
import org.jetbrains.annotations.NotNull;

public class ElectroSlimeRenderer extends MobRenderer<ElectroSlime, ModelElectroSlime<ElectroSlime>> {
    public ElectroSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelElectroSlime<>(context.bakeLayer(ModelElectroSlime.LAYER_LOCATION)), 0.3f);

        this.addLayer(new RenderLayer<>(this) {
            final ResourceLocation LAYER_TEXTURE = new ResourceLocation("er:textures/entities/electro_slime_charging.png");

            @Override
            public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, @NotNull ElectroSlime entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity instanceof AuraContainerInterface auraContainerInterface && (auraContainerInterface.getElements() & (3 << (Element.RenderId.ELECTRO.getId() << 1))) != 0) {
                    VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE));
                    this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0), 1, 1, 1, 1);
                }
            }
        });
    }

    @Override
    protected void scale(@NotNull ElectroSlime slime, PoseStack poseStack, float fl) {
        float f = 0.999F;
        poseStack.scale(0.999F, 0.999F, 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float f1 = 1;
        float f2 = Mth.lerp(fl, slime.oSquish, slime.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ElectroSlime entity) {
        return new ResourceLocation("er:textures/entities/electro_slime.png");
    }
}
