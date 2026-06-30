public class ModelFatuiElectroCicinMage<T extends LivingEntity> extends HumanoidModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "fatui_electro_cicin_mage"), "main");


	public ModelFatuiElectroCicinMage(ModelPart root) {
		super(root);


	}
 	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		createDefaultMageMesh(partdefinition);
		return LayerDefinition.create(meshdefinition, 64, 64);
    }
    
	protected static void createDefaultMageMesh(PartDefinition partdefinition) {

		PartDefinition Head = partdefinition.getChild("head");
		PartDefinition Headdress = Head.addOrReplaceChild("Headdress", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -36.0F, -5.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition headdress_2 = Headdress.addOrReplaceChild("headdress_2",
				CubeListBuilder.create().texOffs(25, 0).addBox(-1.0F, -4.0F, 0.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 0).addBox(-1.0F, -7.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(4.0F, -32.0F, 0.0F));
		PartDefinition headdress_3 = Headdress.addOrReplaceChild("headdress_3", CubeListBuilder.create().texOffs(25, 0).mirror().addBox(-1.0F, -4.0F, 0.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(56, 0).mirror()
				.addBox(0.0F, -7.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, -32.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
				PartPose.offset(-5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
				PartPose.offset(5.0F, 2.0F, 0.0F));


	}
    
	public void prepareMobModel(T p_103793_, float p_103794_, float p_103795_, float p_103796_) {
		this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
		this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
		super.prepareMobModel(p_103793_, p_103794_, p_103795_, p_103796_);
	}
	
	public void translateToHand(HumanoidArm p_103778_, PoseStack p_103779_) {
		float f = p_103778_ == HumanoidArm.RIGHT ? 1.0F : -1.0F;
		ModelPart modelpart = this.getArm(p_103778_);
		modelpart.x += f;
		modelpart.translateAndRotate(p_103779_);
		modelpart.x -= f;
	}
}
