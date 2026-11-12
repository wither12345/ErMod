package net.wither.er.client.renderer.samachurl;

import net.mcreator.er.ErMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.samachurl.Samachurl;
import org.jetbrains.annotations.NotNull;

public class HydroSamachurlRenderer extends SamachurlRenderer {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "textures/entities/samachurl/hydro.png");
    public HydroSamachurlRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Samachurl samachurl) {
        return LOCATION;
    }
}
