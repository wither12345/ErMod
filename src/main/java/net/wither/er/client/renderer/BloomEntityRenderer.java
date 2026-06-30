package net.wither.er.client.renderer;

import net.mcreator.er.client.model.Modelbloom;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.BloomEntityEntity;

public class BloomEntityRenderer extends LivingEntityRenderer<BloomEntityEntity, Modelbloom<BloomEntityEntity>> {
    public BloomEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelbloom<BloomEntityEntity>(context.bakeLayer(Modelbloom.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(BloomEntityEntity entity) {
        return new ResourceLocation("er:textures/entities/bloom_entity.png");
    }

    @Override
    protected boolean shouldShowName(BloomEntityEntity p_115333_) {
        return false;
    }
}
