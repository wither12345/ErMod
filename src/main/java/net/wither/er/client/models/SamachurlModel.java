package net.wither.er.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import net.wither.er.entity.samachurl.Samachurl;
import org.jetbrains.annotations.NotNull;

public class SamachurlModel<T extends Samachurl> extends HumanoidModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private static final float ROT_90 = (float) Math.toRadians(90);
    private static final float ROT_30 = (float) Math.toRadians(30);
    private static final float ROT_15 = (float) Math.toRadians(15);

    private final ModelPart mask;
    private final ModelPart root;

    public SamachurlModel(ModelPart root) {
        super(root);
        this.mask = this.head.getChild("mask");
        this.root = root;
    }
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 10).addBox(-4.0F, -8.0F, 2.0F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(29, 24).addBox(-3.0F, 2.0F, 3.0F, 6.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition mask = head.addOrReplaceChild("mask", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -7.0F, 4.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(38, 0).addBox(-5.0F, -5.0F, 4.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-7.0F, -6.0F, 4.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 40).addBox(-6.0F, -3.0F, 4.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 42).addBox(-1.0F, -1.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 42).addBox(-0.5F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -9.0F));

        PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = hat.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(52, 20).addBox(-1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.0F, 9.0F, 0.0F, 0.3491F, 0.0F));

        PartDefinition cube_r2 = hat.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(52, 20).addBox(0.0F, -1.0F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, 9.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r3 = hat.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(58, 2).addBox(-1.0F, -5.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(-1.0F, -3.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.0F, 3.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition cube_r4 = hat.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(58, 2).addBox(0.0F, -5.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(0.0F, -3.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -8.0F, 3.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition cube_r5 = hat.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(54, 2).addBox(-1.0F, -5.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, -1.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition cube_r6 = hat.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(54, 2).addBox(0.0F, -5.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -8.0F, -1.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, -8.0F, -2.0F, 6.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(34, 3).addBox(-6.0F, 0.0F, -2.0F, 6.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(3.0F, 16.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 16.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(20, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 16.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(4.0F, 8.0F, 0.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(8, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(-4.0F, 8.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void translateToHand(@NotNull HumanoidArm humanoidArm, @NotNull PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        super.translateToHand(humanoidArm, poseStack);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.body.y = 0;
        this.head.y = -8;
        this.hat.copyFrom(this.head);
        switch (entity.getAction()){
            case CONJURE_CONSTANT -> {
                animationCheck(entity, ageInTicks, entity.getAction());
                float t = (int) (ageInTicks - entity.startTick);
                float s = Math.min(1, t / 5);
                this.rightArm.xRot = -ROT_90;
                this.leftArm.xRot = -ROT_90;
                this.rightArm.yRot = -ROT_30 * s;
                this.leftArm.yRot = ROT_30 * s;

                float preZ = (float) (Math.sin(ageInTicks / 2) * ROT_15);

                this.rightLeg.zRot = -preZ;
                this.leftLeg.zRot = -preZ;

                this.root.zRot = preZ;
            }
            case NORMAL -> {
                animationCheck(entity, ageInTicks, entity.getAction());
                this.rightArm.yRot = 0;
                this.leftArm.yRot = 0;
                if(!entity.getMainHandItem().isEmpty())
                    this.rightArm.xRot = -ROT_90;
                else
                    this.rightArm.xRot = 0;
                this.root.zRot = 0;
            }
        }
        this.root.y = 16;
        this.rightLeg.y = 0;
        this.leftLeg.y = 0;
        this.leftArm.y = -8;
        this.rightArm.y = -8;
    }

    private static void animationCheck(Samachurl samachurl, float ageInTicks, Samachurl.Action action){
        if (samachurl.startTick == 0 || samachurl.lastAction != action) {
            samachurl.startTick = ageInTicks;
            samachurl.lastAction = action;
        }
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float v, float v1, float v2, float v3) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, v, v1, v2, v3);
        /*
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
        this.hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
        this.leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
        this.rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
        this.leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
        this.rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);

         */
    }

}
