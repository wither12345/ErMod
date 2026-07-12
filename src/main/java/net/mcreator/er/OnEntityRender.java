/*
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.er as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.er;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(value = {Dist.CLIENT})
public class OnEntityRender {
	private static final Set<LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>> renderedEntities = new HashSet<>();

	@SubscribeEvent
	public static void onEntityRender(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> rend = event.getRenderer();
		if (renderedEntities.contains(rend))
			return;
		rend.addLayer(new FrozenLayer(rend));
		renderedEntities.add(rend);
	}

	public static class FrozenLayer extends RenderLayer<LivingEntity, EntityModel<LivingEntity>> {
		final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("minecraft:textures/block/frosted_ice_0.png");

		public FrozenLayer(LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer) {
			super(renderer);
		}

		@Override
		public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, @NotNull LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (entity instanceof AuraContainerInterface containerInterface && (containerInterface.getElements() & (3 << (Element.RenderId.FROZEN.getId() << 1))) > 0) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(LAYER_TEXTURE));
				this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
			}
		}
	}
}
