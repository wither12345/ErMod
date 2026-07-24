package net.wither.er.client.renderer.whopperflower;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.wither.er.entity.whopperflower.PyroWhopperflowerBall;
import org.jetbrains.annotations.NotNull;

public class PyroWhopperflowerBallRenderer extends EntityRenderer<PyroWhopperflowerBall> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ErMod.MODID, "pyro_flower_ball"), "main");
    private static final ResourceLocation LOCATION = new ResourceLocation(ErMod.MODID, "textures/entities/pyro_flower_ball.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(LOCATION);
    private final ModelPart main;

    public PyroWhopperflowerBallRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(LAYER_LOCATION);
        this.main = modelpart.getChild("main");
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PyroWhopperflowerBall ball) {
        return LOCATION;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 16);
    }

    @Override
    public void render(@NotNull PyroWhopperflowerBall ball, float f, float dt, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int l) {
        int i = OverlayTexture.NO_OVERLAY;
        VertexConsumer vertexConsumer = source.getBuffer(RENDER_TYPE);

        main.render(poseStack, vertexConsumer, l, i);

        super.render(ball, f, dt, poseStack, source, l);
    }
}
