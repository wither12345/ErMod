package net.wither.er.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.er.ErMod;
import net.mcreator.er.StellaFortunas;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.entity.AnimationStater;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;
import org.joml.Quaternionf;

import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ErMod.MODID, value = Dist.CLIENT)
public class AnimatedPlayerModel extends PlayerModel<AbstractClientPlayer> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("er", "animated_player"), "main");
	public static final ModelLayerLocation SLIM_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("er", "slim_animated_player"), "main");
	private final ModelPart root;
	private final ModelPart leftItem;
	private final ModelPart rightItem;

	private final HierarchicalModel<AbstractClientPlayer> animator = new HierarchicalModel<>() {
        @Override
        public ModelPart root() {
            return root;
        }

        @Override
        public void setupAnim(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            Item stellaFortuna = entity.getCapability(ErItemVariables.PLAYER_VARIABLES).orElseGet(ErItemVariables.PlayerVariables::new).Stella_Fortuna.getItem();
            int combo = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new).animationId;
            float speed = (float) entity.getAttribute(Attributes.ATTACK_SPEED).getValue();
            if (stellaFortuna instanceof StellaFortunas SF)
                this.animate(((AnimationStater) entity).getState(), SF.getAnimation(combo), ageInTicks, speed);
        }
    };

	public static LayerDefinition createBodyLayer(CubeDeformation p_170826_, boolean p_170827_) {
		MeshDefinition meshdefinition = PlayerModel.createMesh(p_170826_, p_170827_);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition leftArm = partdefinition.getChild("left_arm");
		PartDefinition rightArm = partdefinition.getChild("right_arm");
		PartDefinition right_item = leftArm.addOrReplaceChild("left_item", CubeListBuilder.create(), PartPose.offset(1.0F, 7.0F, 0.0F));
		PartDefinition left_item = rightArm.addOrReplaceChild("right_item", CubeListBuilder.create(), PartPose.offset(-1.0F, 7.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public AnimatedPlayerModel(ModelPart root, boolean type) {
		super(root, type);
		this.leftItem = root.getChild("left_arm").getChild("left_item");
		this.rightItem = root.getChild("right_arm").getChild("right_item");
		this.root = root;
	}

	protected ModelPart getArmItem(HumanoidArm p_102852_) {
		return p_102852_ == HumanoidArm.LEFT ? this.leftItem : this.rightItem;
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
		//super.translateToHand(arm, poseStack);
		ModelPart handPart = this.getArm(arm);
		handPart.translateAndRotate(poseStack);
		ModelPart itemPart = this.getArmItem(arm);
		//itemPart.translateAndRotate(poseStack);
		poseStack.rotateAround(new Quaternionf().rotationZYX(itemPart.zRot, itemPart.yRot, itemPart.xRot), (itemPart.x) / 16f, (itemPart.y) / 16f, (itemPart.z) / 16f);
	}

	@Override
	public void setupAnim(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		Optional<ErCombatVariables.PlayerVariables> varOpt =  entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).resolve();
		if(varOpt.isPresent()){
			ErCombatVariables.PlayerVariables var = varOpt.get();
			if(var.animationTime > 0) {
				animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				this.leftPants.copyFrom(this.leftLeg);
				this.rightPants.copyFrom(this.rightLeg);
				this.leftSleeve.copyFrom(this.leftArm);
				this.rightSleeve.copyFrom(this.rightArm);
				this.jacket.copyFrom(this.body);
			}
			else {
				this.body.x = 0.0F;
				this.body.z = 0.0F;
				this.rightLeg.x = 2F;
				this.rightLeg.z = 0.0F;
				this.leftLeg.x = -2F;
				this.leftLeg.z = 0.0F;
				this.head.x = 0;
				this.head.y = 0;
				this.head.z = 0;
				this.rightItem.xRot = 0;
				this.rightItem.yRot = 0;
				this.rightItem.zRot = 0;
				super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			}
		}
		else
			super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@SubscribeEvent
	public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(LAYER_LOCATION, () -> createBodyLayer(new CubeDeformation(0.0f), false));
		event.registerLayerDefinition(SLIM_LAYER_LOCATION, () -> createBodyLayer(new CubeDeformation(0.0f), true));
	}
}