package net.wither.er.client.renderer;

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
import net.wither.er.entity.LunarCrystallize;
import org.jetbrains.annotations.NotNull;

public class LunarCrystallizeRenderer extends EntityRenderer<LunarCrystallize> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "lunar_crystallize"), "main");
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "textures/entities/lunar_crystallize.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(LOCATION);
    private final ModelPart main;
    private final ModelPart lunar;

    protected LunarCrystallizeRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(LAYER_LOCATION);
        this.main = modelpart.getChild("main");
        this.lunar = modelpart.getChild("lunar");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(22, 13).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(22, 21).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-0.5F, -9.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition lunar = partdefinition.addOrReplaceChild("lunar", CubeListBuilder.create().texOffs(56, 7).addBox(-0.5F, -20.5F, 1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(36, 4).addBox(-0.5F, -13.5F, 3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(43, 4).addBox(-0.5F, -19.5F, 3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 2).addBox(-0.5F, -19.5F, 4.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(39, 2).addBox(-0.5F, -17.5F, 5.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 3).addBox(-0.5F, -11.5F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(36, 7).addBox(-0.5F, -10.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(42, 8).addBox(-0.5F, -12.5F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(43, 2).addBox(-0.5F, -19.5F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 7).addBox(-0.5F, -12.5F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 9).addBox(-0.5F, -13.5F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, 4).addBox(-0.5F, -21.5F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LunarCrystallize lunarCrystallize) {
        return LOCATION;
    }

    @Override
    public void render(@NotNull LunarCrystallize crystallize, float f, float dt, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int l) {
        int i = OverlayTexture.NO_OVERLAY;
        poseStack.pushPose();
        poseStack.scale(-1,-1,1);
        poseStack.translate(0, -1.501F, 0);
        VertexConsumer vertexConsumer = source.getBuffer(RENDER_TYPE);
        for(int c = 0 ; c < 3 ; c ++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(c * 120));
            poseStack.translate(2.5,0,0);
            renderCrystallize(poseStack, vertexConsumer, l, i);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private void renderCrystallize(PoseStack poseStack, VertexConsumer vertexConsumer, int l, int i){
        main.render(poseStack, vertexConsumer, l , i);
        lunar.render(poseStack, vertexConsumer, l , i);
    }
}
