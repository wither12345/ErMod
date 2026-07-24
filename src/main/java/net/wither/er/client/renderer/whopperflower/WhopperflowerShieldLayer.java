package net.wither.er.client.renderer.whopperflower;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.client.model.WhopperflowerModel;
import net.wither.er.entity.whopperflower.Whopperflower;
import org.jetbrains.annotations.NotNull;

public class WhopperflowerShieldLayer extends RenderLayer<Whopperflower, WhopperflowerModel<Whopperflower>> {
    private final ResourceLocation location;
    public WhopperflowerShieldLayer(WhopperflowerRenderer<Whopperflower> parent, ResourceLocation location) {
        super(parent);
        this.location = location;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull Whopperflower whopperflower, float v, float v1, float v2, float v3, float v4, float v5) {
        if(whopperflower.getAction() == Whopperflower.Action.SHIELD) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.eyes(location));
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(whopperflower, 0));
        }
    }
}
