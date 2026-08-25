package net.wither.er.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.LinkMechanismTelpher;
import org.jetbrains.annotations.NotNull;

public class LinkMechanismTelpherRenderer extends EntityRenderer<LinkMechanismTelpher> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.parse("er:textures/entities/lunar_crystallize_projectile.png");

    public LinkMechanismTelpherRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected int getBlockLightLevel(@NotNull LinkMechanismTelpher telpher, @NotNull BlockPos pos) {
        return 15;
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull LinkMechanismTelpher telpher) {
        return TEXTURE_LOCATION;
    }
}
