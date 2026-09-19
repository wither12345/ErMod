package net.wither.er.client.renderer;

import net.mcreator.er.client.model.Modelbloom;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.BloomEntity;

public class BloomEntityRenderer extends LivingEntityRenderer<BloomEntity, Modelbloom<BloomEntity>> {
    public BloomEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelbloom<>(context.bakeLayer(Modelbloom.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(BloomEntity entity) {
        return ResourceLocation.parse("er:textures/entities/bloom_entity.png");
    }

    @Override
    protected boolean shouldShowName(BloomEntity p_115333_) {
        return false;
    }
}
