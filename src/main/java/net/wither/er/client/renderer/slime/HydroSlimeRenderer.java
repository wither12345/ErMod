package net.wither.er.client.renderer.slime;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.er.client.model.ModelSlime;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.wither.er.entity.slimes.HydroSlime;
import org.jetbrains.annotations.NotNull;

public class HydroSlimeRenderer extends MobRenderer<HydroSlime, ModelSlime<HydroSlime>> {
    public HydroSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelSlime<>(context.bakeLayer(ModelSlime.LAYER_LOCATION)), 0.3f);
    }

    @Override
    protected void scale(@NotNull HydroSlime slime, PoseStack poseStack, float fl) {
        float f = 0.999F;
        poseStack.scale(0.999F, 0.999F, 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float f1 = 1;
        float f2 = Mth.lerp(fl, slime.oSquish, slime.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull HydroSlime entity) {
        return ResourceLocation.parse("er:textures/entities/hydro_slime.png");
    }
}
