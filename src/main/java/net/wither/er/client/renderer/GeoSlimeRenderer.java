package net.wither.er.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.er.client.model.ModelGeoSlime;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.wither.er.entity.slimes.GeoSlime;
import org.jetbrains.annotations.NotNull;

public class GeoSlimeRenderer extends MobRenderer<GeoSlime, ModelGeoSlime<GeoSlime>> {
    public GeoSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelGeoSlime<>(context.bakeLayer(ModelGeoSlime.LAYER_LOCATION)), 0.3f);
    }

    @Override
    protected void scale(@NotNull GeoSlime slime, PoseStack poseStack, float fl) {
        float f = 0.999F;
        poseStack.scale(0.999F, 0.999F, 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float f1 = 1;
        float f2 = Mth.lerp(fl, slime.oSquish, slime.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GeoSlime entity) {
        return ResourceLocation.parse("er:textures/entities/geo_slime.png");
    }
}
