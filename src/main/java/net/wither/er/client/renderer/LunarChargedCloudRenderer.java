package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.er.ErMod;
import net.minecraft.client.Minecraft;
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
import net.wither.er.entity.LunarChargedCloud;
import org.jetbrains.annotations.NotNull;

public class LunarChargedCloudRenderer extends EntityRenderer<LunarChargedCloud> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "lunar_cloud"), "main");
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "textures/entities/lunar_cloud.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(LOCATION);

    private final ModelPart main;
    private final ModelPart cloud1;
    private final ModelPart cloud2;
    private final ModelPart cloud3;
    private final ModelPart cloud4;
    private final ModelPart cloud5;
    private final ModelPart line1;
    private final ModelPart line2;

    protected LunarChargedCloudRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(LAYER_LOCATION);
        this.main = modelpart.getChild("main");
        this.cloud1 = modelpart.getChild("cloud1");
        this.cloud2 = modelpart.getChild("cloud2");
        this.cloud3 = modelpart.getChild("cloud3");
        this.cloud4 = modelpart.getChild("cloud4");
        this.cloud5 = modelpart.getChild("cloud5");
        this.line1 = modelpart.getChild("line1");
        this.line2 = modelpart.getChild("line2");
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LunarChargedCloud lunarChargedCloud) {
        return LOCATION;
    }

    @Override
    public void render(@NotNull LunarChargedCloud cloud, float f, float dt, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int l) {
        int i = OverlayTexture.NO_OVERLAY;
        VertexConsumer vertexConsumer = source.getBuffer(RENDER_TYPE);

        poseStack.pushPose();
        poseStack.scale(4,-4,4);
        poseStack.translate(0, -1.5, 0);
        long time = Minecraft.getInstance().level.getGameTime();

        scale(main , time, dt, 0);
        main.render(poseStack, vertexConsumer, l, i);

        scale(cloud1 , time, dt, 8);
        cloud1.render(poseStack, vertexConsumer, l, i);


        scale(cloud2 , time, dt, 16);
        cloud2.render(poseStack, vertexConsumer, l, i);


        scale(cloud3 , time, dt, 24);
        cloud3.render(poseStack, vertexConsumer, l, i);

        scale(cloud4 , time, dt, 32);
        cloud4.render(poseStack, vertexConsumer, l, i);

        scale(cloud5 , time, dt, 48);
        cloud5.render(poseStack, vertexConsumer, l, i);

        scaleL(line1 , time, dt, 31.4f);
        line1.render(poseStack, vertexConsumer, l, i);
        scaleL(line2 , time, dt, 62.8f);
        line2.render(poseStack, vertexConsumer, l, i);
        poseStack.popPose();
        super.render(cloud, f, dt, poseStack, source, l);
    }

    private static void scale(ModelPart part, long t, float dt, float d){
        float s = (float) (0.1 * Math.sin(0.1 * (t + dt + d)) + 1);
        part.xScale = s;
        part.yScale = s;
        part.zScale = s;
    }

    private static void scaleL(ModelPart part, long t, float dt, float d){
        float m = (float) Math.sin(0.1 * (t + dt + d));
        if(m < 0.75) {
            part.visible = false;
            return;
        }
        part.visible = true;
        float s = 0.1f * m + 1;
        part.xScale = s;
        part.yScale = s;
        part.zScale = s;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.4F)), PartPose.offset(-1.0F, 20.0F, 1.0F));

        PartDefinition cloud1 = partdefinition.addOrReplaceChild("cloud1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(7, 23).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 22.0F, 6.0F));

        PartDefinition cloud2 = partdefinition.addOrReplaceChild("cloud2", CubeListBuilder.create().texOffs(15, 0).addBox(-3.0F, -1.0F, -3.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(7, 22).addBox(-3.0F, -1.0F, -3.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(-6.0F, 22.0F, -6.0F));

        PartDefinition cloud3 = partdefinition.addOrReplaceChild("cloud3", CubeListBuilder.create().texOffs(15, 0).addBox(-4.0F, -1.0F, -1.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(13, 11).addBox(-6.0F, 0.0F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(8, 23).addBox(-4.0F, -1.0F, -1.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.25F))
                .texOffs(16, 22).addBox(-6.0F, 0.0F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 22.0F, -5.0F));

        PartDefinition cloud4 = partdefinition.addOrReplaceChild("cloud4", CubeListBuilder.create().texOffs(17, 2).addBox(-3.0F, -3.0F, -5.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(48, 0).addBox(-4.0F, -2.0F, 1.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(9, 22).addBox(-3.0F, -3.0F, -5.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.25F))
                .texOffs(15, 26).addBox(-4.0F, -2.0F, 1.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(3.0F, 23.0F, 5.0F));

        PartDefinition cloud5 = partdefinition.addOrReplaceChild("cloud5", CubeListBuilder.create().texOffs(26, 0).addBox(-11.0F, -1.0F, 4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(39, 0).addBox(-5.0F, -3.0F, 12.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(8, 22).addBox(-11.0F, -1.0F, 4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.25F))
                .texOffs(13, 26).addBox(-5.0F, -3.0F, 12.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 22.0F, -5.0F));

        PartDefinition line1 = partdefinition.addOrReplaceChild("line1", CubeListBuilder.create().texOffs(13, 40).addBox(-6.0F, -5.0F, -7.0F, 1.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 22.0F, 0.0F));

        PartDefinition line2 = partdefinition.addOrReplaceChild("line2", CubeListBuilder.create().texOffs(31, 34).addBox(2.0F, -4.5F, -6.5F, 1.0F, 7.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 22.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

}
