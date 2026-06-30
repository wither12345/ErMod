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

// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelTornado<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("er", "model_tornado"), "main");
	public final ModelPart main;
	public final ModelPart inside;
	public final ModelPart inside2;
	public final ModelPart inside3;
	public final ModelPart inside4;
	public final ModelPart inside5;
	public final ModelPart inside6;

	public ModelTornado(ModelPart root) {
		this.main = root.getChild("main");
		this.inside = this.main.getChild("inside");
		this.inside2 = this.main.getChild("inside2");
		this.inside3 = this.main.getChild("inside3");
		this.inside4 = this.main.getChild("inside4");
		this.inside5 = this.main.getChild("inside5");
		this.inside6 = this.main.getChild("inside6");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition inside = main.addOrReplaceChild("inside", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition model_4_r1 = inside.addOrReplaceChild("model_4_r1", CubeListBuilder.create().texOffs(0, -32).addBox(10.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 1.1781F, 1.5708F));
		PartDefinition model_3_r1 = inside.addOrReplaceChild("model_3_r1", CubeListBuilder.create().texOffs(0, -32).addBox(10.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 2.7489F));
		PartDefinition model_2_r1 = inside.addOrReplaceChild("model_2_r1", CubeListBuilder.create().texOffs(0, -32).addBox(10.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, -1.1781F, 1.5708F));
		PartDefinition model_1_r1 = inside.addOrReplaceChild("model_1_r1", CubeListBuilder.create().texOffs(0, -32).addBox(10.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
		PartDefinition inside2 = main.addOrReplaceChild("inside2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition model_4_r2 = inside2.addOrReplaceChild("model_4_r2", CubeListBuilder.create().texOffs(0, -32).addBox(10.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 1.1781F, 1.5708F));
		PartDefinition model_3_r2 = inside2.addOrReplaceChild("model_3_r2", CubeListBuilder.create().texOffs(0, -32).addBox(11.0F, -35.0F, -12.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 2.7489F));
		PartDefinition model_2_r2 = inside2.addOrReplaceChild("model_2_r2", CubeListBuilder.create().texOffs(0, -32).addBox(10.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, -1.1781F, 1.5708F));
		PartDefinition model_1_r2 = inside2.addOrReplaceChild("model_1_r2", CubeListBuilder.create().texOffs(0, -32).addBox(10.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
		PartDefinition inside3 = main.addOrReplaceChild("inside3", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));
		PartDefinition model_4_r3 = inside3.addOrReplaceChild("model_4_r3", CubeListBuilder.create().texOffs(0, -32).addBox(23.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, 1.5708F, 1.1781F, 1.5708F));
		PartDefinition model_3_r3 = inside3.addOrReplaceChild("model_3_r3", CubeListBuilder.create().texOffs(0, -32).addBox(23.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, -3.1416F, 0.0F, 2.7489F));
		PartDefinition model_2_r3 = inside3.addOrReplaceChild("model_2_r3", CubeListBuilder.create().texOffs(0, -32).addBox(23.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, -1.5708F, -1.1781F, 1.5708F));
		PartDefinition model_1_r3 = inside3.addOrReplaceChild("model_1_r3", CubeListBuilder.create().texOffs(0, -32).addBox(23.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
		PartDefinition inside4 = main.addOrReplaceChild("inside4", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -12.0F, 1.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition model_4_r4 = inside4.addOrReplaceChild("model_4_r4", CubeListBuilder.create().texOffs(0, -32).addBox(24.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, 1.5708F, 1.1781F, 1.5708F));
		PartDefinition model_3_r4 = inside4.addOrReplaceChild("model_3_r4", CubeListBuilder.create().texOffs(0, -32).addBox(23.0F, -35.0F, -11.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, -3.1416F, 0.0F, 2.7489F));
		PartDefinition model_2_r4 = inside4.addOrReplaceChild("model_2_r4", CubeListBuilder.create().texOffs(0, -32).addBox(21.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, -1.5708F, -1.1781F, 1.5708F));
		PartDefinition model_1_r4 = inside4.addOrReplaceChild("model_1_r4", CubeListBuilder.create().texOffs(0, -32).addBox(23.0F, -35.0F, -17.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
		PartDefinition inside5 = main.addOrReplaceChild("inside5", CubeListBuilder.create(), PartPose.offset(0.0F, -46.0F, 0.0F));
		PartDefinition model_4_r5 = inside5.addOrReplaceChild("model_4_r5", CubeListBuilder.create().texOffs(0, -32).addBox(36.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, 1.5708F, 1.1781F, 1.5708F));
		PartDefinition model_3_r5 = inside5.addOrReplaceChild("model_3_r5", CubeListBuilder.create().texOffs(0, -32).addBox(36.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, -3.1416F, 0.0F, 2.7489F));
		PartDefinition model_2_r5 = inside5.addOrReplaceChild("model_2_r5", CubeListBuilder.create().texOffs(0, -32).addBox(36.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, -1.5708F, -1.1781F, 1.5708F));
		PartDefinition model_1_r5 = inside5.addOrReplaceChild("model_1_r5", CubeListBuilder.create().texOffs(0, -32).addBox(36.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
		PartDefinition inside6 = main.addOrReplaceChild("inside6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -46.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition model_4_r6 = inside6.addOrReplaceChild("model_4_r6", CubeListBuilder.create().texOffs(0, -32).addBox(36.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, 1.5708F, 1.1781F, 1.5708F));
		PartDefinition model_3_r6 = inside6.addOrReplaceChild("model_3_r6", CubeListBuilder.create().texOffs(0, -32).addBox(36.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, -3.1416F, 0.0F, 2.7489F));
		PartDefinition model_2_r6 = inside6.addOrReplaceChild("model_2_r6", CubeListBuilder.create().texOffs(0, -32).addBox(36.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, -1.5708F, -1.1781F, 1.5708F));
		PartDefinition model_1_r6 = inside6.addOrReplaceChild("model_1_r6", CubeListBuilder.create().texOffs(0, -32).addBox(36.0F, -35.0F, -16.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}