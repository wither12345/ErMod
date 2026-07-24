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
import net.wither.er.entity.whopperflower.CryoSpike;
import org.jetbrains.annotations.NotNull;

public class CryoSpikeRenderer extends EntityRenderer<CryoSpike> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "cryo_spike"), "main");
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "textures/entities/cryo_spike.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(LOCATION);

    private final ModelPart mian;
    private final ModelPart spike_l;
    private final ModelPart spike_r;
    private final ModelPart spike_l2;
    private final ModelPart spike_r2;

    public CryoSpikeRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(LAYER_LOCATION);
        this.mian = modelpart;
        ModelPart group = modelpart.getChild("group");
        this.spike_l = group.getChild("spike_l");
        this.spike_r = group.getChild("spike_r");
        ModelPart group2 = modelpart.getChild("group2");
        this.spike_l2 = group2.getChild("spike_l2");
        this.spike_r2 = group2.getChild("spike_r2");
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CryoSpike cryoSpike) {
        return LOCATION;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition group = partdefinition.addOrReplaceChild("group", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, -3.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition spike_l = group.addOrReplaceChild("spike_l", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(-0.5F, -14.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.48F));

        PartDefinition spike_r = group.addOrReplaceChild("spike_r", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(-0.5F, -14.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition group2 = partdefinition.addOrReplaceChild("group2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 5.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition spike_l2 = group2.addOrReplaceChild("spike_l2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(-0.5F, -14.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.48F));

        PartDefinition spike_r2 = group2.addOrReplaceChild("spike_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(-0.5F, -14.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.5236F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void render(@NotNull CryoSpike cryoSpike, float f, float dt, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int l) {
        poseStack.pushPose();
        poseStack.scale(1,-1, 1);
        poseStack.translate(0 ,-1.5 ,0);
        VertexConsumer vertexConsumer = source.getBuffer(RENDER_TYPE);
        int i = OverlayTexture.NO_OVERLAY;
        this.mian.yRot = (float) (- cryoSpike.getYRot() / 180 * Math.PI);
        this.spike_r.y = 10 - Math.min(10, dt + cryoSpike.time * 2);
        this.spike_l.y = 10 - Math.min(10, dt + cryoSpike.time * 2 - 0.5f);
        //this.group2.yRot = (float) (- cryoSpike.getYRot() / 180 * Math.PI);
        this.spike_r2.y = 10 - Math.min(10, dt + cryoSpike.time * 2 - 1);
        this.spike_l2.y = 10 - Math.min(10, dt + cryoSpike.time * 2 - 1.5f);
        mian.render(poseStack, vertexConsumer, l, i);
        poseStack.popPose();
    }


}
