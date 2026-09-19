package net.wither.er.client.renderer.hypostasiscube;

import net.mcreator.er.ErMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.hypostasiscube.ElectroHypostasisCube;
import org.jetbrains.annotations.NotNull;

public class ElectroHypostasisCubeRenderer extends AbstractHypostasisCubeRenderer<ElectroHypostasisCube> {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "textures/entities/electro_hypostasis_cube.png");

    public ElectroHypostasisCubeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getCubeLocation() {
        return LOCATION;
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ElectroHypostasisCube electroHypostasisCube) {
        return LOCATION;
    }
}
