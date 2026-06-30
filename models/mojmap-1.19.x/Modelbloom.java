// Made with Blockbench 4.7.0
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelbloom<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "bloom"), "main");
	private final ModelPart main;

	public Modelbloom(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition rootstock = main.addOrReplaceChild("rootstock",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-1.0F, -13.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 0)
						.addBox(-3.0F, -10.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(2, 2)
						.addBox(-1.0F, -10.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(2, 2)
						.addBox(-1.0F, -10.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition fruit = main.addOrReplaceChild("fruit",
				CubeListBuilder.create().texOffs(0, 5)
						.addBox(-3.0F, -9.0F, -2.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 11)
						.addBox(-4.0F, -8.0F, -3.0F, 7.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 21)
						.addBox(-3.0F, -5.0F, -2.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(10, 6)
						.addBox(-2.0F, -3.0F, -1.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(16, 0)
						.addBox(-1.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition crisperd_one = main.addOrReplaceChild("crisperd_one",
				CubeListBuilder.create().texOffs(6, 2)
						.addBox(-0.5F, -9.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 0)
						.addBox(-0.5F, -8.0F, 3.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 5)
						.addBox(-0.5F, -5.0F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(-0.5F, -3.0F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 2)
						.addBox(-0.5F, -2.0F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.5F, 0.0F, 0.5F));

		PartDefinition crisperd_two = main.addOrReplaceChild("crisperd_two",
				CubeListBuilder.create().texOffs(6, 2)
						.addBox(-0.5F, -9.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 0)
						.addBox(-0.5F, -8.0F, 3.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 5)
						.addBox(-0.5F, -5.0F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(-0.5F, -3.0F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 2)
						.addBox(-0.5F, -2.0F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, 0.0F, 0.5F, 0.0F, -1.5708F, 0.0F));

		PartDefinition crisperd_three = main.addOrReplaceChild("crisperd_three",
				CubeListBuilder.create().texOffs(6, 2)
						.addBox(-0.5F, -9.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 0)
						.addBox(-0.5F, -8.0F, 3.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 5)
						.addBox(-0.5F, -5.0F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(-0.5F, -3.0F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 2)
						.addBox(-0.5F, -2.0F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, 0.0F, 0.5F, 0.0F, 3.1416F, 0.0F));

		PartDefinition crisperd_four = main.addOrReplaceChild("crisperd_four",
				CubeListBuilder.create().texOffs(6, 2)
						.addBox(-0.5F, -9.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 0)
						.addBox(-0.5F, -8.0F, 3.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 5)
						.addBox(-0.5F, -5.0F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(-0.5F, -3.0F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 2)
						.addBox(-0.5F, -2.0F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, 0.0F, 0.5F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.main.yRot = ageInTicks / 20.f;
	}
}