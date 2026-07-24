package net.wither.er.client.renderer.whopperflower;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.wither.er.client.models.WhopperflowerModel;
import net.wither.er.entity.whopperflower.Whopperflower;
import org.jetbrains.annotations.NotNull;

public class WhopperflowerItemLayer<T extends Whopperflower, M extends WhopperflowerModel<T>> extends RenderLayer<T, M> {
    private final ItemRenderer itemRenderer;
    public WhopperflowerItemLayer(RenderLayerParent<T, M> parent, ItemRenderer itemRenderer) {
        super(parent);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T whopperflower, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = whopperflower.getFruitItem();
        boolean flag = whopperflower.getAction() == Whopperflower.Action.CONSUMING || whopperflower.getAction() == Whopperflower.Action.SHIELD;
        for (int c = whopperflower.getFruitCount(); c > 0; c--) {
            poseStack.pushPose();
            poseStack.scale(-0.4f, -0.4f, 0.4f);
            float dx = 2 * (float) Math.sin(Math.PI * 2 * c / 3);
            float dz = 2 * (float) Math.cos(Math.PI * 2 * c / 3);
            float dy = 0 ;

            if(flag && c == 1){
                float p = 1 - Math.min((ageInTicks - whopperflower.animationStart) / 6, 1);
                dx *= p;
                dz *= p;
                dy = 1.5f * (1 - p);
            }

            poseStack.rotateAround(Axis.YP.rotation(ageInTicks / 2), dx, 0, dz);

            poseStack.translate(dx, dy, dz);
            this.renderItem(whopperflower, stack, poseStack, multiBufferSource, i);
            poseStack.popPose();
        }
    }
    


    protected void renderItem(Whopperflower whopperflower, ItemStack itemStack, PoseStack poseStack, MultiBufferSource source, int l) {
        if (!itemStack.isEmpty()) {
            this.itemRenderer.renderStatic(whopperflower, itemStack, ItemDisplayContext.FIXED, true, poseStack, source, whopperflower.level(), l, 0, 0);
        }
    }
}
