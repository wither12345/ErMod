package net.wither.er.client.renderer.hypostasiscube;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.er.ErMod;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.hypostasiscube.ElectroCubeProjectile;
import org.jetbrains.annotations.NotNull;

public class ElectroCubeProjectileRenderer extends EntityRenderer<ElectroCubeProjectile> {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "textures/entities/electro_hypostasis_cube.png");

    private final ModelPart model;
    private final RenderType renderType;
    private final RenderType renderTypeL;
    public ElectroCubeProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = context.bakeLayer(AbstractHypostasisCubeRenderer.LAYER_LOCATION);
        this.renderType = RenderType.entityTranslucent(LOCATION);
        this.renderTypeL = RenderType.eyes(LOCATION);
    }

    @Override
    public void render(@NotNull ElectroCubeProjectile projectile, float f, float dt, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int l) {
        super.render(projectile, f, dt, poseStack, source, l);
        this.model.render(poseStack, source.getBuffer(renderType), l, OverlayTexture.NO_OVERLAY);
        this.model.render(poseStack, source.getBuffer(renderTypeL), l, OverlayTexture.NO_OVERLAY);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ElectroCubeProjectile electroCubeProjectile) {
        return LOCATION;
    }
}
