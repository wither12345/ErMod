package net.wither.er.client.renderer.hypostasiscube;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.er.ErMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.client.renderer.RenderVertex;
import net.wither.er.entity.hypostasiscube.ElectroCubeCrystal;
import org.jetbrains.annotations.NotNull;

public class ElectroCubeCrystalRenderer extends EntityRenderer<ElectroCubeCrystal> {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "textures/entities/electro_hypostasis_cube.png");

    private final RenderType renderType;
    private final RenderType renderTypeE;
    public ElectroCubeCrystalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.renderType = RenderType.entityTranslucent(LOCATION);
        this.renderTypeE = RenderType.eyes(LOCATION);
    }

    @Override
    public void render(@NotNull ElectroCubeCrystal cubeCrystal, float f, float dt, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int l) {
        super.render(cubeCrystal, f, dt, poseStack, source, l);
        int i = OverlayTexture.NO_OVERLAY;
        poseStack.pushPose();
        poseStack.scale(0.6f, 0.6f, 0.6f);
        poseStack.translate(0, 0.6, 0);
        //poseStack.translate(0, 0.6, 0);
        VertexConsumer vertexConsumer = source.getBuffer(renderType);
        PoseStack.Pose pose = poseStack.last();

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        RenderVertex.vertex(vertexConsumer, pose, i, 0.0F, 0, 0.75f, 0.5f);
        RenderVertex.vertex(vertexConsumer, pose, i, 1F, 0, 1, 0.5f);
        RenderVertex.vertex(vertexConsumer, pose, i, 1F, 1, 1, 0);
        RenderVertex.vertex(vertexConsumer, pose, i, 0.0F, 1, 0.75f, 0);
        vertexConsumer = source.getBuffer(renderTypeE);
        RenderVertex.vertex(vertexConsumer, pose, i, 0.0F, 0, 0.75f, 0.5f);
        RenderVertex.vertex(vertexConsumer, pose, i, 1F, 0, 1, 0.5f);
        RenderVertex.vertex(vertexConsumer, pose, i, 1F, 1, 1, 0);
        RenderVertex.vertex(vertexConsumer, pose, i, 0.0F, 1, 0.75f, 0);
        poseStack.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ElectroCubeCrystal cubeCrystal) {
        return LOCATION;
    }
}
