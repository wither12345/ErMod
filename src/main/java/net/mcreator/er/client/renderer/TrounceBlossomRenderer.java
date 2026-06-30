package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HierarchicalModel;

import net.mcreator.er.entity.TrounceBlossomEntity;
import net.mcreator.er.client.model.animations.TrounceBlossomAnimation;
import net.mcreator.er.client.model.ModelTrounceBlossom;

public class TrounceBlossomRenderer extends MobRenderer<TrounceBlossomEntity, ModelTrounceBlossom<TrounceBlossomEntity>> {
	public TrounceBlossomRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(ModelTrounceBlossom.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(TrounceBlossomEntity entity) {
		return ResourceLocation.parse("er:textures/entities/ley_line_blossom_revelation.png");
	}

	private static final class AnimatedModel extends ModelTrounceBlossom<TrounceBlossomEntity> {
		private final ModelPart root;
		private final HierarchicalModel animator = new HierarchicalModel<TrounceBlossomEntity>() {
			@Override
			public ModelPart root() {
				return root;
			}

			@Override
			public void setupAnim(TrounceBlossomEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
				this.root().getAllParts().forEach(ModelPart::resetPose);
				this.animate(entity.animationState0, TrounceBlossomAnimation.claim, ageInTicks, 1f);
			}
		};

		public AnimatedModel(ModelPart root) {
			super(root);
			this.root = root;
		}

		@Override
		public void setupAnim(TrounceBlossomEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}
}