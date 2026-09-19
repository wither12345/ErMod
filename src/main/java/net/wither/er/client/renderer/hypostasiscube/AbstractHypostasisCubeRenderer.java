package net.wither.er.client.renderer.hypostasiscube;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mcreator.er.ErMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.client.renderer.RenderVertex;
import net.wither.er.entity.hypostasiscube.HypostasisCube;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHypostasisCubeRenderer<T extends HypostasisCube> extends EntityRenderer<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "hypostasis_cube"), "main");

    private final ModelPart model;
    private final RenderType renderTypeE;
    private final RenderType renderType;
    protected AbstractHypostasisCubeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = context.bakeLayer(LAYER_LOCATION);
        this.renderType = RenderType.entityTranslucent(this.getCubeLocation());
        this.renderTypeE = RenderType.eyes(this.getCubeLocation());
    }

    public abstract ResourceLocation getCubeLocation();

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -32.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void render(@NotNull T cube, float f, float dt, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int l) {
        super.render(cube, f, dt, poseStack, source, l);
        int i = OverlayTexture.NO_OVERLAY;
        HypostasisCubeState state = cube.getState();

        poseStack.pushPose();
        poseStack.translate(0, 0.5, 0);
        VertexConsumer vertexConsumer = source.getBuffer(renderType);
        poseStack.rotateAround(Axis.YP.rotation(-(float) Math.toRadians(f)),0,0,0);

        HypostasisCubeState.Turning turning = cube.getTurning();
        if(turning != null)
            state.renderTurning(cube, this.model, turning, poseStack, vertexConsumer, l, i, dt);
        else
            state.render(this.model, poseStack, vertexConsumer, l, i, cube.getAnimateTime(true), dt);

        VertexConsumer vertexConsumerE = source.getBuffer(renderTypeE);
        if(turning != null)
            state.renderTurning(cube, this.model, turning, poseStack, vertexConsumerE, l, i, dt);
        else
            state.render(this.model, poseStack, vertexConsumerE, l, i, cube.getAnimateTime(true), dt);
        poseStack.popPose();


        poseStack.pushPose();
        poseStack.scale(0.6f, 0.6f, 0.6f);
        poseStack.translate(0, 0.6, 0);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        PoseStack.Pose pose = poseStack.last();
        RenderVertex.vertex(vertexConsumerE, pose, i, 0.0F, 0, 0, 0.5f);
        RenderVertex.vertex(vertexConsumerE, pose, i, 1F, 0, 0.25f, 0.5f);
        RenderVertex.vertex(vertexConsumerE, pose, i, 1F, 1, 0.25f, 0);
        RenderVertex.vertex(vertexConsumerE, pose, i, 0.0F, 1, 0, 0);
        poseStack.popPose();
    }
}
