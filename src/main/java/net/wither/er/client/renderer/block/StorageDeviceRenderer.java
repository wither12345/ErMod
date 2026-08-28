package net.wither.er.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.block.entity.StorageDeviceEntity;
import org.jetbrains.annotations.NotNull;

public class StorageDeviceRenderer implements BlockEntityRenderer<StorageDeviceEntity> {
    private final ItemRenderer itemRenderer;
    public StorageDeviceRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull StorageDeviceEntity entity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        Level level = entity.getLevel();
        if(level == null) return;
        float r = (v + level.getGameTime()) * 0.2f;
        for(int j = 0; j < entity.getContainerSize(); j ++){
            poseStack.pushPose();
            poseStack.translate(getXOffset(j), 0.9, getZOffset(j));
            poseStack.scale(0.2f, 0.2f, 0.2f);
            poseStack.mulPose(Axis.YP.rotation(r));
            ItemStack stack = entity.getItem(j);
            this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, i, i1, poseStack, multiBufferSource, level, 0);
            poseStack.popPose();
        }
    }

    private static double getXOffset(int index){
        return 0.2 * (index / 3) + 0.3;
    }

    private static double getZOffset(int index){
        return 0.2 * (index % 3) + 0.3;
    }
}
