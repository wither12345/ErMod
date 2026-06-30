// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelGeoSlime<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "geoslime"), "main");
	private final ModelPart body;
	private final ModelPart Lhron;
	private final ModelPart Rhron;

	public ModelGeoSlime(ModelPart root) {
		this.body = root.getChild("body");
		this.Lhron = this.body.getChild("Lhron");
		this.Rhron = this.body.getChild("Rhron");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-5.0F, -8.0F, -5.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Lhron = body.addOrReplaceChild("Lhron",
				CubeListBuilder.create().texOffs(30, 1)
						.addBox(1.0F, -11.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(31, 2)
						.addBox(2.0F, -13.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Rhron = body.addOrReplaceChild("Rhron",
				CubeListBuilder.create().texOffs(30, 1)
						.addBox(-4.0F, -11.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(31, 2)
						.addBox(-4.0F, -13.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}