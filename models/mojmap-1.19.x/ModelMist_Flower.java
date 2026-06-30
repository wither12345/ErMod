// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelMist_Flower<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "mist_flower"), "main");
	private final ModelPart leaves;
	private final ModelPart roots;
	private final ModelPart flowers;

	public ModelMist_Flower(ModelPart root) {
		this.leaves = root.getChild("leaves");
		this.roots = root.getChild("roots");
		this.flowers = root.getChild("flowers");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition leaves = partdefinition.addOrReplaceChild("leaves", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition leaf3_r1 = leaves
				.addOrReplaceChild("leaf3_r1",
						CubeListBuilder.create().texOffs(11, 6).addBox(-1.5F, -7.0F, 1.0F, 2.0F, 0.0F, 3.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition leaf2_r1 = leaves
				.addOrReplaceChild("leaf2_r1",
						CubeListBuilder.create().texOffs(11, 6).addBox(-1.5F, -7.0F, -1.0F, 2.0F, 0.0F, 3.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition roots = partdefinition.addOrReplaceChild("roots", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition root2_r1 = roots
				.addOrReplaceChild("root2_r1",
						CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.0F, -2.0F, 1.0F, 3.0F, 1.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition root1_r1 = roots
				.addOrReplaceChild("root1_r1",
						CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -12.0F, 0.0F, 1.0F, 8.0F, 1.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition flowers = partdefinition.addOrReplaceChild("flowers", CubeListBuilder.create(),
				PartPose.offset(0.0F, 15.0F, 3.0F));

		PartDefinition flower4_r1 = flowers.addOrReplaceChild("flower4_r1",
				CubeListBuilder.create().texOffs(0, 17)
						.addBox(-2.0F, 1.0F, -8.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(24, 12)
						.addBox(-1.5F, 0.0F, -8.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition flower2_r1 = flowers.addOrReplaceChild("flower2_r1",
				CubeListBuilder.create().texOffs(12, 12)
						.addBox(-2.5F, -3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 12)
						.addBox(-2.0F, -5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		leaves.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		roots.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		flowers.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}