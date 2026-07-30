package net.wither.er.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.whopperflower.Whopperflower;
import org.jetbrains.annotations.NotNull;

// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class WhopperflowerModel<T extends Whopperflower> extends EntityModel<T> {
    private static final float STUN_X = -(float) (Math.PI / 4);
    private static final float FIRE_X = -(float) (Math.PI / 6);
    private static final float FIRE_CONSTANT_X = (float) Math.PI / 3;
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation PYRO = new ModelLayerLocation(new ResourceLocation("er", "pyro_whopperflower"), "main");
    public static final ModelLayerLocation CRYO = new ModelLayerLocation(new ResourceLocation("er", "cryo_whopperflower"), "main");
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
    private final PetalGroup petals;

	public WhopperflowerModel(ModelPart root) {
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
		this.leaves = this.body.getChild("leaves");
		this.leave_1 = this.leaves.getChild("leave_1");
		this.bone = this.leave_1.getChild("bone");
		this.leave_2 = this.leaves.getChild("leave_2");
		this.bone7 = this.leave_2.getChild("bone7");
		this.leave_3 = this.leaves.getChild("leave_3");
		this.bone8 = this.leave_3.getChild("bone8");
		this.leave_4 = this.leaves.getChild("leave_4");
		this.bone9 = this.leave_4.getChild("bone9");
        this.head = root.getChild("head");
        this.petals = new PetalGroup(this.head.getChild("petals"));
	}


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(42, 0).addBox(-2.0F, -14.0F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(-3.0F, -11.0F, -5.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-1.0F, -16.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 30).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 1.0F));
        PartDefinition leave1 = body.addOrReplaceChild("leave1", CubeListBuilder.create().texOffs(18, 21).addBox(0.0F, -1.0F, -1.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.0F, 1.0F, -0.9163F, 0.4363F, 0.0F));
        PartDefinition cube_r1 = leave1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(44, 7).addBox(-1.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 3.0F, 0.3054F, 0.0F, 0.0F));
        PartDefinition leave2 = body.addOrReplaceChild("leave2", CubeListBuilder.create().texOffs(46, 13).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -6.0F, 1.0F, -0.9163F, -0.4363F, 0.0F));
        PartDefinition cube_r2 = leave2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 17).addBox(-1.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 3.0F, 0.3054F, 0.0F, 0.0F));
        PartDefinition leave3 = body.addOrReplaceChild("leave3", CubeListBuilder.create().texOffs(40, 32).addBox(-5.0F, 0.0F, -3.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-2.0F, -1.0F, -3.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -12.0F, 0.0F, 0.0F, 0.0F, -1.0472F));
        PartDefinition leave4 = body.addOrReplaceChild("leave4", CubeListBuilder.create().texOffs(40, 35).addBox(-1.0F, 0.0F, -2.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 46).addBox(-1.0F, -1.0F, -2.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -12.0F, -1.0F, 0.0F, 0.0F, 1.0472F));
        PartDefinition leave5 = body.addOrReplaceChild("leave5", CubeListBuilder.create().texOffs(7, 45).addBox(-1.5F, -1.0F, -7.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -15.0F, -3.0F, 1.0472F, 0.0F, 0.0F));
        PartDefinition leave6 = body.addOrReplaceChild("leave6", CubeListBuilder.create().texOffs(31, 32).addBox(-1.5F, -1.0F, 0.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -15.0F, -1.0F, -1.0472F, 0.0F, 0.0F));
        PartDefinition leave7 = body.addOrReplaceChild("leave7", CubeListBuilder.create().texOffs(0, 36).addBox(-7.0F, -1.0F, -1.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -15.0F, -1.0F, 0.0F, 0.0F, -0.7854F));
        PartDefinition leave8 = body.addOrReplaceChild("leave8", CubeListBuilder.create().texOffs(0, 36).addBox(0.0F, -1.0F, -1.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -15.0F, -1.0F, 0.0F, 0.0F, 0.7854F));
        PartDefinition leave9 = body.addOrReplaceChild("leave9", CubeListBuilder.create().texOffs(54, 20).addBox(0.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -6.0F, -2.0F, 0.0F, 0.0F, 0.7854F));
        PartDefinition cube_r3 = leave9.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 24).addBox(0.0F, -1.0F, -5.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 3.0F, 0.0F, 0.0F, -0.1745F));
        PartDefinition leave10 = body.addOrReplaceChild("leave10", CubeListBuilder.create().texOffs(24, 27).addBox(-3.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -6.0F, -2.0F, 0.0F, 0.0F, -0.7854F));
        PartDefinition cube_r4 = leave10.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 19).addBox(-5.0F, -1.0F, -5.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.1745F));
        PartDefinition leave11 = body.addOrReplaceChild("leave11", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, -6.0F, -4.0F, 0.0F, -0.5236F, 0.0F));
        PartDefinition cube_r5 = leave11.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(32, 29).addBox(-5.0F, -1.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6109F));
        PartDefinition leave12 = body.addOrReplaceChild("leave12", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -6.0F, -4.0F, 0.0F, 0.5236F, 0.0F));
        PartDefinition cube_r6 = leave12.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(46, 24).addBox(0.0F, -1.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));
        PartDefinition leaves = body.addOrReplaceChild("leaves", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -16.0F, -1.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition leave_1 = leaves.addOrReplaceChild("leave_1", CubeListBuilder.create().texOffs(0, 42).addBox(1.0F, 0.5F, -1.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 0.0F, 0.0F, -0.2618F));
        PartDefinition bone = leave_1.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(11, 39).addBox(0.0F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
        PartDefinition cube_r7 = bone.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(35, 41).addBox(3.0F, 0.5F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 40).addBox(0.0F, 0.5F, -0.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition leave_2 = leaves.addOrReplaceChild("leave_2", CubeListBuilder.create().texOffs(0, 42).addBox(1.0F, 0.5F, -1.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 1.5708F, -1.309F, -1.5708F));
        PartDefinition bone7 = leave_2.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(11, 39).addBox(0.0F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
        PartDefinition cube_r8 = bone7.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(35, 41).addBox(3.0F, 0.5F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 40).addBox(0.0F, 0.5F, -0.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition leave_3 = leaves.addOrReplaceChild("leave_3", CubeListBuilder.create().texOffs(0, 42).addBox(1.0F, 0.5F, -1.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 3.1416F, 0.0F, -2.8798F));
        PartDefinition bone8 = leave_3.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(11, 39).addBox(0.0F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
        PartDefinition cube_r9 = bone8.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(35, 41).addBox(3.0F, 0.5F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 40).addBox(0.0F, 0.5F, -0.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition leave_4 = leaves.addOrReplaceChild("leave_4", CubeListBuilder.create().texOffs(0, 42).addBox(1.0F, 0.5F, -1.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, -1.5708F, 1.309F, -1.5708F));
        PartDefinition bone9 = leave_4.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(11, 39).addBox(0.0F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
        PartDefinition cube_r10 = bone9.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(35, 41).addBox(3.0F, 0.5F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 40).addBox(0.0F, 0.5F, -0.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-1.5F, -7.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));
        PartDefinition petals = head.addOrReplaceChild("petals", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));
        PartDefinition petal1 = petals.addOrReplaceChild("petal1", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, -1.0F));
        PartDefinition cube_r11 = petal1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(27, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.6545F, 0.0F, 0.0F));
        PartDefinition cube_r12 = petal1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.309F, 0.0F, 0.0F));
        PartDefinition bone1 = petal1.addOrReplaceChild("bone1", CubeListBuilder.create().texOffs(25, 9).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.5F, 6.0F, -0.2618F, 0.0F, 0.0F));
        PartDefinition bone2 = bone1.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(12, 16).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 7.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition cube_r13 = bone2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(23, 17).addBox(-1.0F, 0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, -0.6545F, 0.0F, 0.0F));
        PartDefinition petal2 = petals.addOrReplaceChild("petal2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.0F, -1.0F, 0.0F, -2.0944F, 0.0F));
        PartDefinition cube_r14 = petal2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(27, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.6545F, 0.0F, 0.0F));
        PartDefinition cube_r15 = petal2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.309F, 0.0F, 0.0F));
        PartDefinition bone3 = petal2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(25, 9).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.5F, 6.0F, -0.2618F, 0.0F, 0.0F));
        PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(12, 16).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 7.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition cube_r16 = bone4.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(23, 17).addBox(-1.0F, 0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, -0.6545F, 0.0F, 0.0F));
        PartDefinition petal3 = petals.addOrReplaceChild("petal3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.0F, -1.0F, 0.0F, 2.0944F, 0.0F));
        PartDefinition cube_r17 = petal3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(27, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.6545F, 0.0F, 0.0F));
        PartDefinition cube_r18 = petal3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.309F, 0.0F, 0.0F));
        PartDefinition bone5 = petal3.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(25, 9).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.5F, 6.0F, -0.2618F, 0.0F, 0.0F));
        PartDefinition bone6 = bone5.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(12, 16).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 7.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition cube_r19 = bone6.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(23, 17).addBox(-1.0F, 0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, -0.6545F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, float v, float v1, float v2, float v3) {
        body.render(poseStack, vertexConsumer, i, i1, v, v1, v2, v3);
		head.render(poseStack, vertexConsumer, i, i1, v, v1, v2, v3);
	}

	public void setupAnim(@NotNull Whopperflower whopperflower, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = 0;
        this.head.xRot = 0;
        this.head.zRot = 0;
        this.leaves.yRot = 0;
        this.leaves.xRot = 0;
        Whopperflower.Action action = whopperflower.getAction();
        petals.reSet();
        switch (whopperflower.getAction()) {
            case CLOSING, CONSUMING -> {
                animationCheck(whopperflower, ageInTicks, action);
                petals.closing((ageInTicks - whopperflower.animationStart) / 5);
            }
            case OPENING -> {
                animationCheck(whopperflower, ageInTicks, action);
                petals.opening((ageInTicks - whopperflower.animationStart) / 5);
            }
            case SPIN -> {
                animationCheck(whopperflower, ageInTicks, action);
                petals.spin(ageInTicks - whopperflower.animationStart);
            }
            case FIRE -> {
                animationCheck(whopperflower, ageInTicks, action);
                this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
                float dt = ageInTicks - whopperflower.animationStart;
                if(dt > 4){
                    dt -= 4;
                    petals.opening(dt);
                }else
                    petals.closing(dt);


                float x = -FIRE_X + (float)Math.sin((ageInTicks - whopperflower.animationStart) * 0.3f) * 0.2f;
                this.head.xRot = x;
                this.leaves.xRot = x * 0.6f;
            }
            case STUN -> {
                animationCheck(whopperflower, ageInTicks, action);
                float x = -STUN_X + (float)Math.sin((ageInTicks - whopperflower.animationStart) * 0.3f) * 0.2f;
                float y = (float)Math.cos((ageInTicks - whopperflower.animationStart) * 0.3f) * 0.2f;
                this.head.xRot = x;
                this.head.yRot = y;
                this.leaves.xRot = x * 0.6f;
                this.leaves.yRot = y * 0.6f;
            }
            case FIRE_CONSTANT -> {
                animationCheck(whopperflower, ageInTicks, action);
                this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
                float dt = ageInTicks - whopperflower.animationStart;
                if(dt > 4){
                    dt -= 4;
                    this.head.xRot = FIRE_CONSTANT_X ;
                    petals.fireConstant(dt);
                }
                else {
                    this.head.xRot = FIRE_CONSTANT_X * dt / 4 ;
                    petals.fireConstantPrepare(dt / 4);
                }
            }
            case LOWER_HEAD -> {
                animationCheck(whopperflower, ageInTicks, action);
                float dt = ageInTicks - whopperflower.animationStart;
                if(dt > 2)
                    this.head.xRot = FIRE_CONSTANT_X ;
                else
                    this.head.xRot = FIRE_CONSTANT_X * dt / 2 ;
            }
            case SHIELD -> {
                animationCheck(whopperflower, ageInTicks, action);
                petals.close();
                this.head.zRot = (float) Math.sin((ageInTicks - whopperflower.animationStart) * 0.4) * 0.05f;
            }
            case DOWN, UP -> {
                whopperflower.animationStart = 0;
                petals.close();
            }
            default -> {
                this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
                this.head.xRot = headPitch / (180F / (float) Math.PI);
                whopperflower.animationStart = 0;
                petals.open();
            }
        }
    }

    private static void animationCheck(Whopperflower whopperflower, float ageInTicks, Whopperflower.Action action){
        if (whopperflower.animationStart == 0 || whopperflower.lastAction != action) {
            whopperflower.animationStart = ageInTicks;
            whopperflower.lastAction = action;
        }
    }

    private record PetalGroup(ModelPart main, Petal petal1, Petal petal2, Petal petal3){
        private static final float DIVIDE = (float) (Math.PI / 3) * 2;
        private static final float NORMAL_0 = 0;
        private static final float NORMAL_1 = -(float) (Math.PI / 12);
        private static final float NORMAL_2 = -(float) (Math.PI / 180) * 20;
        private static final float CLOSE_1 = (float) (Math.PI / 180) * 115;
        private static final float CLOSE_2 = (float) (Math.PI / 180) * 25;
        private static final float SPIN_X_0 = -(float) (Math.PI / 180) * 35;
        private static final float SPIN_X_1 = (float) (Math.PI / 180) * 50;
        private static final float SPIN_X_2 = (float) (Math.PI / 180) * 5;
        private static final float SPIN_Y_0 = -(float) Math.PI / 2;
        private static final float SPIN_Y_1 = (float) Math.PI * 2;
        private static final float FIRE_CONSTANT_X = (float) Math.PI / 180 * 100;
        private static final float FIRE_CONSTANT_MAIN_X = (float) Math.PI / 6;


        public PetalGroup(ModelPart part){
            this(
                    part,
                    Petal.get(part, 0),
                    Petal.get(part, 1),
                    Petal.get(part, 2)
            );
        }

        public void reSet(){
            main.xRot = 0;
            petal1.yRot(0);
            petal2.yRot(DIVIDE);
            petal3.yRot(-DIVIDE);
            petal1.scale(1);
            petal2.scale(1);
            petal3.scale(1);
        }

        public void open(){
            petal1.boneXRot(NORMAL_1, NORMAL_2);
            petal2.boneXRot(NORMAL_1, NORMAL_2);
            petal3.boneXRot(NORMAL_1, NORMAL_2);
        }

        public void close(){
            petal1.boneXRot(CLOSE_1, CLOSE_2);
            petal2.boneXRot(CLOSE_1, CLOSE_2);
            petal3.boneXRot(CLOSE_1, CLOSE_2);
        }

        public void opening(float p){
            if(p > 1) p = 1;
            float c1 = CLOSE_1 * (1 - p) + NORMAL_1 * p;
            float c2 = CLOSE_2 * (1 - p) + NORMAL_2 * p;
            petal1.boneXRot(c1, c2);
            petal2.boneXRot(c1, c2);
            petal3.boneXRot(c1, c2);
        }

        public void closing(float p){
            if(p > 1) p = 1;
            float c1 = NORMAL_1 * (1 - p) + CLOSE_1 * p;
            float c2 = NORMAL_2 * (1 - p) + CLOSE_2 * p;
            petal1.boneXRot(c1, c2);
            petal2.boneXRot(c1, c2);
            petal3.boneXRot(c1, c2);
        }

        public void fireConstant(float dt){
            main.xRot = FIRE_CONSTANT_MAIN_X;
            petal1.yRot(dt);
            petal2.yRot(dt + DIVIDE);
            petal3.yRot(dt - DIVIDE);
            petal1.boneXRot(FIRE_CONSTANT_X, 0);
            petal2.boneXRot(FIRE_CONSTANT_X, 0);
            petal3.boneXRot(FIRE_CONSTANT_X, 0);
        }

        public void fireConstantPrepare(float p){
            if(p > 1) p = 1;
            float c1 = NORMAL_1 * (1 - p) + FIRE_CONSTANT_X * p;
            petal1.boneXRot(c1, 0);
            petal2.boneXRot(c1, 0);
            petal3.boneXRot(c1, 0);
        }

        public void spin(float t) {
            if(t < 6){
                float p = t / 6;
                float c0 = NORMAL_0 * (1 - p) + SPIN_X_0 * p;
                float c1 = NORMAL_1 * (1 - p) + SPIN_X_1 * p;
                float c2 = NORMAL_2 * (1 - p) + SPIN_X_2 * p;
                petal1.allXRot(c0, c1, c2);
                petal2.allXRot(c0, c1, c2);
                petal3.allXRot(c0, c1, c2);
                petal1.scale(1 + t / 12);
                petal2.scale(1 + t / 12);
                petal3.scale(1 + t / 12);
                float r = SPIN_Y_0 / t ;
                petal1.yRot(-r);
                petal2.yRot(-r + DIVIDE);
                petal3.yRot(-r - DIVIDE);
            }
            else if(t < 12){
                float r = SPIN_Y_0 + SPIN_Y_1 * (t - 6) / 6 ;
                petal1.allXRot(SPIN_X_0, SPIN_X_1, SPIN_X_2);
                petal2.allXRot(SPIN_X_0, SPIN_X_1, SPIN_X_2);
                petal3.allXRot(SPIN_X_0, SPIN_X_1, SPIN_X_2);
                petal1.scale(1.5f);
                petal2.scale(1.5f);
                petal3.scale(1.5f);
                petal1.yRot(r);
                petal2.yRot(r + DIVIDE);
                petal3.yRot(r - DIVIDE);
            }
            else {
                float p = Math.min((t - 12) / 5, 1) ;
                float s = 1.5f - p / 2;
                float c0 = SPIN_X_0 * (1 - p) + NORMAL_0 * p;
                float c1 = SPIN_X_1 * (1 - p) + NORMAL_1 * p;
                float c2 = SPIN_X_2 * (1 - p) + NORMAL_2 * p;
                petal1.scale(s);
                petal2.scale(s);
                petal3.scale(s);
                petal1.allXRot(c0, c1, c2);
                petal2.allXRot(c0, c1, c2);
                petal3.allXRot(c0, c1, c2);
            }
        }
    }

    private record Petal(ModelPart petal, ModelPart bone1, ModelPart bone2) {
        public static Petal get(ModelPart part, int id){
            ModelPart p0 = part.getChild("petal" + (id + 1));
            ModelPart p1 = p0.getChild("bone" + (id * 2 + 1));
            return new Petal(p0, p1, p1.getChild("bone" + (id * 2 + 2)));
        }

        public void scale(float s){
            petal.xScale = s ;
            petal.yScale = s ;
            petal.zScale = s ;
        }

        public void yRot(float rot){
            petal.yRot = rot;
        }

        public void allXRot(float rot0, float rot1, float rot2){
            petal.xRot = rot0;
            bone1.xRot = rot1;
            bone2.xRot = rot2;
        }

        public void boneXRot(float rot1, float rot2){
            petal.xRot = 0;
            bone1.xRot = rot1;
            bone2.xRot = rot2;
        }
    }
}