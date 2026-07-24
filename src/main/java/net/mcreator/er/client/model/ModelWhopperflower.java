package net.mcreator.er.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelWhopperflower<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("er", "model_whopperflower"), "main");
	public final ModelPart body;
	public final ModelPart leave1;
	public final ModelPart leave2;
	public final ModelPart leave3;
	public final ModelPart leave4;
	public final ModelPart leave5;
	public final ModelPart leave6;
	public final ModelPart leave7;
	public final ModelPart leave8;
	public final ModelPart leave9;
	public final ModelPart leave10;
	public final ModelPart leave11;
	public final ModelPart leave12;
	public final ModelPart head;
	public final ModelPart leaves;
	public final ModelPart leave_1;
	public final ModelPart bone;
	public final ModelPart leave_2;
	public final ModelPart bone7;
	public final ModelPart leave_3;
	public final ModelPart bone8;
	public final ModelPart leave_4;
	public final ModelPart bone9;
	public final ModelPart petals;
	public final ModelPart petal1;
	public final ModelPart bone1;
	public final ModelPart bone2;
	public final ModelPart petal2;
	public final ModelPart bone3;
	public final ModelPart bone4;
	public final ModelPart petal3;
	public final ModelPart bone5;
	public final ModelPart bone6;

	public ModelWhopperflower(ModelPart root) {
		this.body = root.getChild("body");
		this.leave1 = this.body.getChild("leave1");
		this.leave2 = this.body.getChild("leave2");
		this.leave3 = this.body.getChild("leave3");
		this.leave4 = this.body.getChild("leave4");
		this.leave5 = this.body.getChild("leave5");
		this.leave6 = this.body.getChild("leave6");
		this.leave7 = this.body.getChild("leave7");
		this.leave8 = this.body.getChild("leave8");
		this.leave9 = this.body.getChild("leave9");
		this.leave10 = this.body.getChild("leave10");
		this.leave11 = this.body.getChild("leave11");
		this.leave12 = this.body.getChild("leave12");
		this.head = root.getChild("head");
		this.leaves = this.head.getChild("leaves");
		this.leave_1 = this.leaves.getChild("leave_1");
		this.bone = this.leave_1.getChild("bone");
		this.leave_2 = this.leaves.getChild("leave_2");
		this.bone7 = this.leave_2.getChild("bone7");
		this.leave_3 = this.leaves.getChild("leave_3");
		this.bone8 = this.leave_3.getChild("bone8");
		this.leave_4 = this.leaves.getChild("leave_4");
		this.bone9 = this.leave_4.getChild("bone9");
		this.petals = this.head.getChild("petals");
		this.petal1 = this.petals.getChild("petal1");
		this.bone1 = this.petal1.getChild("bone1");
		this.bone2 = this.bone1.getChild("bone2");
		this.petal2 = this.petals.getChild("petal2");
		this.bone3 = this.petal2.getChild("bone3");
		this.bone4 = this.bone3.getChild("bone4");
		this.petal3 = this.petals.getChild("petal3");
		this.bone5 = this.petal3.getChild("bone5");
		this.bone6 = this.bone5.getChild("bone6");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(42, 0).addBox(-2.0F, -14.0F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 21).addBox(-3.0F, -11.0F, -5.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 4)
						.addBox(-1.0F, -16.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(18, 30).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 1.0F));
		PartDefinition leave1 = body.addOrReplaceChild("leave1", CubeListBuilder.create().texOffs(18, 21).addBox(0.0F, -1.0F, -1.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.0F, -6.0F, 1.0F, -0.9163F, 0.4363F, 0.0F));
		PartDefinition cube_r1 = leave1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(44, 7).addBox(-1.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 3.0F, 0.3054F, 0.0F, 0.0F));
		PartDefinition leave2 = body.addOrReplaceChild("leave2", CubeListBuilder.create().texOffs(46, 13).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, -6.0F, 1.0F, -0.9163F, -0.4363F, 0.0F));
		PartDefinition cube_r2 = leave2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 17).addBox(-1.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 3.0F, 0.3054F, 0.0F, 0.0F));
		PartDefinition leave3 = body.addOrReplaceChild("leave3",
				CubeListBuilder.create().texOffs(40, 32).addBox(-5.0F, 0.0F, -3.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 38).addBox(-2.0F, -1.0F, -3.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.0F, -12.0F, 0.0F, 0.0F, 0.0F, -1.0472F));
		PartDefinition leave4 = body.addOrReplaceChild("leave4",
				CubeListBuilder.create().texOffs(40, 35).addBox(-1.0F, 0.0F, -2.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 46).addBox(-1.0F, -1.0F, -2.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.0F, -12.0F, -1.0F, 0.0F, 0.0F, 1.0472F));
		PartDefinition leave5 = body.addOrReplaceChild("leave5", CubeListBuilder.create().texOffs(7, 45).addBox(-1.5F, -1.0F, -7.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -15.0F, -3.0F, 1.0472F, 0.0F, 0.0F));
		PartDefinition leave6 = body.addOrReplaceChild("leave6", CubeListBuilder.create().texOffs(31, 32).addBox(-1.5F, -1.0F, 0.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -15.0F, -1.0F, -1.0472F, 0.0F, 0.0F));
		PartDefinition leave7 = body.addOrReplaceChild("leave7", CubeListBuilder.create().texOffs(0, 36).addBox(-7.0F, -1.0F, -1.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, -15.0F, -1.0F, 0.0F, 0.0F, -0.7854F));
		PartDefinition leave8 = body.addOrReplaceChild("leave8", CubeListBuilder.create().texOffs(0, 36).addBox(0.0F, -1.0F, -1.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -15.0F, -1.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition leave9 = body.addOrReplaceChild("leave9", CubeListBuilder.create().texOffs(54, 20).addBox(0.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -6.0F, -2.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition cube_r3 = leave9.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 24).addBox(0.0F, -1.0F, -5.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.0F, 0.0F, 3.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition leave10 = body.addOrReplaceChild("leave10", CubeListBuilder.create().texOffs(24, 27).addBox(-3.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.0F, -6.0F, -2.0F, 0.0F, 0.0F, -0.7854F));
		PartDefinition cube_r4 = leave10.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 19).addBox(-5.0F, -1.0F, -5.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.1745F));
		PartDefinition leave11 = body.addOrReplaceChild("leave11", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, -6.0F, -4.0F, 0.0F, -0.5236F, 0.0F));
		PartDefinition cube_r5 = leave11.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(32, 29).addBox(-5.0F, -1.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6109F));
		PartDefinition leave12 = body.addOrReplaceChild("leave12", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -6.0F, -4.0F, 0.0F, 0.5236F, 0.0F));
		PartDefinition cube_r6 = leave12.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(46, 24).addBox(0.0F, -1.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));
		PartDefinition head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-1.5F, -7.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 5.0F, 0.0F));
		PartDefinition leaves = head.addOrReplaceChild("leaves", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition leave_1 = leaves.addOrReplaceChild("leave_1", CubeListBuilder.create().texOffs(0, 42).addBox(1.0F, 0.5F, -1.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 0.0F, 0.0F, -0.2618F));
		PartDefinition bone = leave_1.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(11, 39).addBox(0.0F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition cube_r7 = bone.addOrReplaceChild("cube_r7",
				CubeListBuilder.create().texOffs(35, 41).addBox(3.0F, 0.5F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 40).addBox(0.0F, 0.5F, -0.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition leave_2 = leaves.addOrReplaceChild("leave_2", CubeListBuilder.create().texOffs(0, 42).addBox(1.0F, 0.5F, -1.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 1.5708F, -1.309F, -1.5708F));
		PartDefinition bone7 = leave_2.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(11, 39).addBox(0.0F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition cube_r8 = bone7.addOrReplaceChild("cube_r8",
				CubeListBuilder.create().texOffs(35, 41).addBox(3.0F, 0.5F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 40).addBox(0.0F, 0.5F, -0.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition leave_3 = leaves.addOrReplaceChild("leave_3", CubeListBuilder.create().texOffs(0, 42).addBox(1.0F, 0.5F, -1.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 3.1416F, 0.0F, -2.8798F));
		PartDefinition bone8 = leave_3.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(11, 39).addBox(0.0F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition cube_r9 = bone8.addOrReplaceChild("cube_r9",
				CubeListBuilder.create().texOffs(35, 41).addBox(3.0F, 0.5F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 40).addBox(0.0F, 0.5F, -0.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition leave_4 = leaves.addOrReplaceChild("leave_4", CubeListBuilder.create().texOffs(0, 42).addBox(1.0F, 0.5F, -1.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, -1.5708F, 1.309F, -1.5708F));
		PartDefinition bone9 = leave_4.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(11, 39).addBox(0.0F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		PartDefinition cube_r10 = bone9.addOrReplaceChild("cube_r10",
				CubeListBuilder.create().texOffs(35, 41).addBox(3.0F, 0.5F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 40).addBox(0.0F, 0.5F, -0.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition petals = head.addOrReplaceChild("petals", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));
		PartDefinition petal1 = petals.addOrReplaceChild("petal1", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, -1.0F));
		PartDefinition cube_r11 = petal1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(27, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.6545F, 0.0F, 0.0F));
		PartDefinition cube_r12 = petal1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.309F, 0.0F, 0.0F));
		PartDefinition bone1 = petal1.addOrReplaceChild("bone1", CubeListBuilder.create().texOffs(25, 9).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.5F, 6.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition bone2 = bone1.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(12, 16).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 7.0F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r13 = bone2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(23, 17).addBox(-1.0F, 0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, -0.6545F, 0.0F, 0.0F));
		PartDefinition petal2 = petals.addOrReplaceChild("petal2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.0F, -1.0F, 0.0F, -2.0944F, 0.0F));
		PartDefinition cube_r14 = petal2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(27, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.6545F, 0.0F, 0.0F));
		PartDefinition cube_r15 = petal2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.309F, 0.0F, 0.0F));
		PartDefinition bone3 = petal2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(25, 9).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.5F, 6.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(12, 16).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 7.0F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r16 = bone4.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(23, 17).addBox(-1.0F, 0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, -0.6545F, 0.0F, 0.0F));
		PartDefinition petal3 = petals.addOrReplaceChild("petal3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.0F, -1.0F, 0.0F, 2.0944F, 0.0F));
		PartDefinition cube_r17 = petal3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(27, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.6545F, 0.0F, 0.0F));
		PartDefinition cube_r18 = petal3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.309F, 0.0F, 0.0F));
		PartDefinition bone5 = petal3.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(25, 9).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.5F, 6.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition bone6 = bone5.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(12, 16).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 7.0F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r19 = bone6.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(23, 17).addBox(-1.0F, 0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, -0.6545F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
	}
}