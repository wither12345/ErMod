package net.wither.er.client.renderer.samachurl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.er.ErMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.samachurl.RainCloud;
import org.jetbrains.annotations.NotNull;

public class RainCloudRenderer extends EntityRenderer<RainCloud> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "lunar_cloud"), "main");
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "textures/entities/lunar_cloud.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(LOCATION);

    private final ModelPart main;
    private final ModelPart cloud1;
    private final ModelPart cloud2;
    private final ModelPart cloud3;
    private final ModelPart cloud4;
    private final ModelPart cloud5;

    public RainCloudRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(LAYER_LOCATION);
        this.main = modelpart.getChild("main");
        this.cloud1 = modelpart.getChild("cloud1");
        this.cloud2 = modelpart.getChild("cloud2");
        this.cloud3 = modelpart.getChild("cloud3");
        this.cloud4 = modelpart.getChild("cloud4");
        this.cloud5 = modelpart.getChild("cloud5");
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull RainCloud rainCloud) {
        return LOCATION;
    }

    @Override
    public void render(@NotNull RainCloud cloud, float f, float dt, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int l) {
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
}
