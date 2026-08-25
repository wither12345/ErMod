package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mcreator.er.ErMod;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.block.LinkMechanismBase;
import net.wither.er.block.entity.LinkMechanismBaseEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LinkMechanismBaseRenderer implements BlockEntityRenderer<LinkMechanismBaseEntity> {
    @Nullable public static LinkMechanismBaseEntity closestBase = null;
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("er", "link_mechanism"), "main");
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "textures/entities/link_mechanism.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(LOCATION);

    private final ModelPart body;
    public LinkMechanismBaseRenderer(BlockEntityRendererProvider.Context context){
        body = context.bakeLayer(LAYER_LOCATION);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -5.0F, 0.0F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -13.0F, 0.0F, -0.2618F, 0.0F, 0.4363F));
        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 6).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -18.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(24, 12).addBox(1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -21.0F, 0.0F, -0.0873F, 0.0F, 0.5236F));
        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(18, 23).addBox(-1.0F, -5.0F, 0.0F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -18.0F, 0.0F, -0.2618F, 0.0F, 0.4363F));
        PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(6, 14).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -14.0F, 0.0F, 0.0F, 0.0F, -1.1345F));
        PartDefinition cube_r6 = body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(18, 12).addBox(0.0F, -9.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -9.0F, -2.0F, 0.6981F, 0.0F, -1.0472F));
        PartDefinition cube_r7 = body.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition spike = body.addOrReplaceChild("spike", CubeListBuilder.create().texOffs(12, 14).addBox(-1.0F, -17.0F, 0.0F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 0).addBox(-3.0F, -12.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -19.0F, 0.0F, -0.1309F, 0.0F, 0.1309F));

        PartDefinition spike2 = body.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(18, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -12.0F, -2.0F, 3.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.9163F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void render(@NotNull LinkMechanismBaseEntity baseEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        BlockState state = baseEntity.getBlockState();
        if(state.getValue(BlockStateProperties.PERSISTENT)) {
            Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            moveByDirection(poseStack, direction);
            poseStack.pushPose();
            poseStack.translate(0, 3.5, 0);
            poseStack.scale(-1, -1, 1);

            disToAim(baseEntity, direction);

            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RENDER_TYPE);
            body.render(poseStack, vertexConsumer, i, i1);
            poseStack.popPose();
        }
    }

    private static void moveByDirection(PoseStack poseStack, Direction direction){
        switch (direction) {
            case SOUTH -> poseStack.translate(1, 0, 1);
            case WEST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(-90));
                poseStack.translate(1, 0, 0);
            }
            case EAST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                poseStack.translate(0, 0, 1);
            }
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
        }
    }

    private static void disToAim(LinkMechanismBaseEntity entity, Direction direction){
        Player localPlayer = Minecraft.getInstance().player;
        if(localPlayer == null) return;
        Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;

        Vec3 center = LinkMechanismBase.getCenter(entity.getBlockPos(), direction);
        double d = center.subtract(localPlayer.position()).lengthSqr();
        if(d > 256 || d < 1) {
            entity.cosToPlayer = -1;
            return;
        }

        Vec3 vec1 = center.subtract(camera.getPosition());
        Vec3 vec2 = new Vec3(camera.getLookVector());

        double shade = vec1.dot(vec2);
        entity.cosToPlayer = shade * shade / vec1.lengthSqr() / vec2.lengthSqr();
        if ((closestBase == null || entity.cosToPlayer > closestBase.cosToPlayer) && entity.cosToPlayer > 0.95) {
            closestBase = entity ;
        }
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull LinkMechanismBaseEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos().above(3));
    }
}
