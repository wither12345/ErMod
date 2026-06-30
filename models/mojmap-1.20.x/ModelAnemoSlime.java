// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelAnemoSlime<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "anemoslime"), "main");
	private final ModelPart body;
	private final ModelPart Lwing;
	private final ModelPart Rwing;

	public ModelAnemoSlime(ModelPart root) {
		this.body = root.getChild("body");
		this.Lwing = root.getChild("Lwing");
		this.Rwing = root.getChild("Rwing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-5.0F, -8.0F, -5.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Lwing = partdefinition.addOrReplaceChild("Lwing",
				CubeListBuilder.create().texOffs(0, 4).addBox(0.0F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(6.0F, 18.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition Rwing = partdefinition.addOrReplaceChild("Rwing",
				CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.0F, -1.0F, 3.0F, 3.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0F, 18.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

		this.Lwing.zRot = (Mth.sin(ageInTicks * 0.6F + 3) * 0.6F);
		this.Rwing.zRot = (Mth.sin(ageInTicks * 0.6F) * 0.6F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Lwing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Rwing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}