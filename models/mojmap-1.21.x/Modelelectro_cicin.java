// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelelectro_cicin<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "electro_cicin"), "main");
	private final ModelPart Body;
	private final ModelPart Right_Wing;
	private final ModelPart Left_Wing;

	public Modelelectro_cicin(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Right_Wing = this.Body.getChild("Right_Wing");
		this.Left_Wing = this.Body.getChild("Left_Wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-2.0F, -9.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 15)
						.addBox(1.0F, -11.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 15)
						.addBox(-2.0F, -11.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Right_Wing = Body.addOrReplaceChild("Right_Wing", CubeListBuilder.create(),
				PartPose.offset(-1.0F, -7.0F, 0.0F));

		PartDefinition Right_Wing_Behind_r1 = Right_Wing.addOrReplaceChild(
				"Right_Wing_Behind_r1", CubeListBuilder.create().texOffs(8, 14).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 1.0F,
						0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 2.0F, 1.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition Right_Wing_Front_r1 = Right_Wing.addOrReplaceChild(
				"Right_Wing_Front_r1", CubeListBuilder.create().texOffs(0, 8).addBox(-14.0F, -3.0F, -1.0F, 14.0F, 3.0F,
						0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Left_Wing = Body.addOrReplaceChild("Left_Wing", CubeListBuilder.create(),
				PartPose.offset(1.0F, -7.0F, 0.0F));

		PartDefinition Left_Wing_Behind_r1 = Left_Wing.addOrReplaceChild(
				"Left_Wing_Behind_r1", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, -1.0F, -1.0F, 4.0F, 1.0F,
						0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Left_Wing_Front_r1 = Left_Wing.addOrReplaceChild(
				"Left_Wing_Front_r1", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -3.0F, -1.0F, 14.0F, 3.0F,
						0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.7854F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.Right_Wing.yRot = (Mth.sin(ageInTicks * 0.6F) * 0.6F);
		this.Left_Wing.yRot = (Mth.sin(ageInTicks * 0.6F + 3) * 0.6F);
		this.Body.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.Body.xRot = headPitch / (180F / (float) Math.PI);
	}
}