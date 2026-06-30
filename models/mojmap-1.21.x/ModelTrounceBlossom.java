// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelTrounceBlossom<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "trounceblossom"), "main");
	private final ModelPart Trunk;
	private final ModelPart Flower;
	private final ModelPart Petal1;
	private final ModelPart Petal2;
	private final ModelPart Petal3;
	private final ModelPart Petal4;
	private final ModelPart Petal5;
	private final ModelPart Petal6;
	private final ModelPart Petal7;
	private final ModelPart Petal8;

	public ModelTrounceBlossom(ModelPart root) {
		this.Trunk = root.getChild("Trunk");
		this.Flower = root.getChild("Flower");
		this.Petal1 = this.Flower.getChild("Petal1");
		this.Petal2 = this.Flower.getChild("Petal2");
		this.Petal3 = this.Flower.getChild("Petal3");
		this.Petal4 = this.Flower.getChild("Petal4");
		this.Petal5 = this.Flower.getChild("Petal5");
		this.Petal6 = this.Flower.getChild("Petal6");
		this.Petal7 = this.Flower.getChild("Petal7");
		this.Petal8 = this.Flower.getChild("Petal8");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Trunk = partdefinition.addOrReplaceChild("Trunk",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-8.0F, -6.0F, -1.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(2.0F, -8.0F, 2.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(0.0F, -8.0F, -8.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(-7.0F, -8.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(-6.0F, -10.0F, -5.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(1.0F, -13.0F, 0.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(-5.0F, -14.0F, -6.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(0.0F, -19.0F, -3.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 14)
						.addBox(-5.0F, -22.0F, 1.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(2.0F, -12.0F, -5.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Flower = partdefinition.addOrReplaceChild("Flower", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Petal1 = Flower.addOrReplaceChild("Petal1",
				CubeListBuilder.create().texOffs(32, 0)
						.addBox(-6.0F, -4.0F, -5.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(40, 0)
						.addBox(-6.0F, 1.0F, -4.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(32, 0)
						.addBox(-5.0F, 3.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -13.0F, 6.0F));

		PartDefinition Petal2 = Flower.addOrReplaceChild("Petal2",
				CubeListBuilder.create().texOffs(48, 8)
						.addBox(-6.0F, -3.0F, -5.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(40, 6)
						.addBox(-5.0F, -5.0F, -5.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(50, 0)
						.addBox(-4.0F, -7.0F, -4.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(32, 3)
						.addBox(-3.0F, -8.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -21.0F, 6.0F));

		PartDefinition Petal3 = Flower.addOrReplaceChild("Petal3",
				CubeListBuilder.create().texOffs(32, 11)
						.addBox(-6.0F, -1.0F, -9.0F, 1.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(58, 4)
						.addBox(-5.0F, 0.0F, -11.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(56, 10)
						.addBox(-4.0F, 1.0F, -12.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -21.0F, 6.0F));

		PartDefinition Petal4 = Flower.addOrReplaceChild("Petal4",
				CubeListBuilder.create().texOffs(39, 17)
						.addBox(-6.0F, -1.0F, 0.0F, 1.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(32, 22)
						.addBox(-5.0F, 0.0F, 5.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(32, 11)
						.addBox(-4.0F, 1.0F, 7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -21.0F, 6.0F));

		PartDefinition Petal5 = Flower.addOrReplaceChild("Petal5",
				CubeListBuilder.create().texOffs(0, 32)
						.addBox(-4.0F, -1.0F, -3.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(45, 22)
						.addBox(-6.0F, -2.0F, -3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(46, 17)
						.addBox(-8.0F, -3.0F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(46, 1)
						.addBox(-10.0F, -4.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-6.0F, -15.0F, 4.0F));

		PartDefinition Petal6 = Flower.addOrReplaceChild("Petal6",
				CubeListBuilder.create().texOffs(26, 28)
						.addBox(-4.0F, 0.0F, -3.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(40, 29)
						.addBox(-6.0F, 1.0F, -3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(50, 30)
						.addBox(-8.0F, 2.0F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(55, 25)
						.addBox(-10.0F, 3.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-6.0F, -23.0F, 4.0F));

		PartDefinition Petal7 = Flower.addOrReplaceChild("Petal7",
				CubeListBuilder.create().texOffs(0, 39)
						.addBox(-4.0F, -3.0F, 0.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 39)
						.addBox(-6.0F, -3.0F, 1.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 33)
						.addBox(-8.0F, -2.0F, 2.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 35)
						.addBox(-10.0F, -1.0F, 3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-6.0F, -19.0F, 0.0F));

		PartDefinition Petal8 = Flower.addOrReplaceChild("Petal8",
				CubeListBuilder.create().texOffs(16, 39)
						.addBox(-4.0F, -3.0F, -1.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 39)
						.addBox(-6.0F, -3.0F, -2.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(31, 35)
						.addBox(-8.0F, -2.0F, -3.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 32)
						.addBox(-10.0F, -1.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-6.0F, -19.0F, 8.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		Trunk.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Flower.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}