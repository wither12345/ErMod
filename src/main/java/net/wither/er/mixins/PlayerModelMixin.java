package net.wither.er.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.er.StellaFortunas;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.wither.er.entity.AnimationStater;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> {
    @Unique
    private ModelPart er$root;
    @Unique private ModelPart er$leftItem;
    @Unique private ModelPart er$rightItem;
    @Final @Shadow public ModelPart leftSleeve;
    @Final @Shadow public ModelPart rightSleeve;
    @Final @Shadow public ModelPart leftPants;
    @Final @Shadow public ModelPart rightPants;
    @Final @Shadow public ModelPart jacket;

    public PlayerModelMixin(ModelPart p_170677_) {super(p_170677_);}


    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(ModelPart root, boolean slim, CallbackInfo info) {
        this.er$root = root;
        this.er$leftItem = root.getChild("left_arm").getChild("left_item");
        this.er$rightItem = root.getChild("right_arm").getChild("right_item");
    }

    @Unique
    private final HierarchicalModel<LivingEntity> er$animator = new HierarchicalModel<>() {
        @Override
        public @NotNull ModelPart root() {
            return er$root;
        }

        @Override
        public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            Item stellaFortuna = entity.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna.getItem();
            int animationId = entity.getData(ErCombatVariables.PLAYER_VARIABLES).animationId;
            float speed = (float) entity.getAttributeValue(Attributes.ATTACK_SPEED);
            if (stellaFortuna instanceof StellaFortunas SF)
                this.animate(((AnimationStater) entity).getState(), SF.getAnimation(animationId), ageInTicks, speed);
        }
    };

    @Inject(method = "createMesh", at = @At("RETURN"), cancellable = true)
    private static void createMesh(CubeDeformation cubeDeformation, boolean slim, CallbackInfoReturnable<MeshDefinition> cir) {
        MeshDefinition meshDefinition = cir.getReturnValue();
        PartDefinition partdefinition = meshDefinition.getRoot();
        PartDefinition leftArm = partdefinition.getChild("left_arm");
        PartDefinition rightArm = partdefinition.getChild("right_arm");
        PartDefinition right_item = leftArm.addOrReplaceChild("left_item", CubeListBuilder.create(), PartPose.offset(1.0F, 7.0F, 0.0F));
        PartDefinition left_item = rightArm.addOrReplaceChild("right_item", CubeListBuilder.create(), PartPose.offset(-1.0F, 7.0F, 0.0F));
        cir.setReturnValue(meshDefinition);
    }

    @Inject(method = "translateToHand", at = @At("HEAD"), cancellable = true)
    public void translateToHand(HumanoidArm arm, PoseStack poseStack, CallbackInfo ci) {
        ModelPart handPart = this.getArm(arm);
        handPart.translateAndRotate(poseStack);
        ModelPart itemPart = this.er$getArmItem(arm);
        poseStack.rotateAround(new Quaternionf().rotationZYX(itemPart.zRot, itemPart.yRot, itemPart.xRot), (itemPart.x) / 16f, (itemPart.y) / 16f, (itemPart.z) / 16f);
        ci.cancel();

    }

    @Unique
    private ModelPart er$getArmItem(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.er$leftItem : this.er$rightItem;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"), cancellable = true)
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ErCombatVariables.PlayerVariables data = entity.getData(ErCombatVariables.PLAYER_VARIABLES);
        if(data.animationTime > 0) {
            er$animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.leftPants.copyFrom(this.leftLeg);
            this.rightPants.copyFrom(this.rightLeg);
            this.leftSleeve.copyFrom(this.leftArm);
            this.rightSleeve.copyFrom(this.rightArm);
            this.jacket.copyFrom(this.body);
            ci.cancel();
        }
        else {
            this.body.x = 0.0F;
            this.body.z = 0.0F;
            this.body.xRot = 0.0F;
            this.body.zRot = 0.0F;
            this.rightLeg.x = 2F;
            this.rightLeg.z = 0.0F;
            this.leftLeg.x = -2F;
            this.leftLeg.z = 0.0F;
            this.head.x = 0;
            this.head.y = 0;
            this.head.z = 0;
            this.er$rightItem.xRot = 0;
            this.er$rightItem.yRot = 0;
            this.er$rightItem.zRot = 0;
            //super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
