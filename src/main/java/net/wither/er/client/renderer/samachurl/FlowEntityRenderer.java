package net.wither.er.client.renderer.samachurl;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.samachurl.FlowEntity;
import org.jetbrains.annotations.NotNull;

public class FlowEntityRenderer extends EntityRenderer<FlowEntity> {
    public FlowEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FlowEntity flowEntity) {
        return null;
    }
}
