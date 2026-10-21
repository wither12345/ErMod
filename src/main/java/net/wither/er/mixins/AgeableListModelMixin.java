package net.wither.er.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.wither.er.client.models.IPlayerModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableListModel.class)
public class AgeableListModelMixin {
    @Inject(method = "renderToBuffer", at = @At(value = "HEAD"))
    public void RenderToBufferHead(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, float v, float v1, float v2, float v3, CallbackInfo ci) {
        if(this instanceof IPlayerModel playerModel) {
            poseStack.pushPose();
            playerModel.er$getRoot().translateAndRotate(poseStack);
        }
    }

    @Inject(method = "renderToBuffer", at = @At(value = "TAIL"))
    public void RenderToBufferTail(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, float v, float v1, float v2, float v3, CallbackInfo ci) {
        if(this instanceof IPlayerModel) {
            poseStack.popPose();
        }
    }
}
