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

// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelFlaming_Flower<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("er", "model_flaming_flower"), "main");
	public final ModelPart leaves;
	public final ModelPart roots;
	public final ModelPart flowers;

	public ModelFlaming_Flower(ModelPart root) {
		this.leaves = root.getChild("leaves");
		this.roots = root.getChild("roots");
		this.flowers = root.getChild("flowers");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition leaves = partdefinition.addOrReplaceChild("leaves", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(40, 6)
				.addBox(-2.0F, -2.0F, -3.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(46, 0).addBox(-1.0F, -4.0F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition roots = partdefinition.addOrReplaceChild("roots", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -9.0F, -1.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition flowers = partdefinition.addOrReplaceChild("flowers",
				CubeListBuilder.create().texOffs(30, 0).addBox(-1.5F, -10.0F, -2.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(30, 5).addBox(-1.0F, -12.0F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 16);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int rgb) {
		leaves.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
		roots.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
		flowers.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
}