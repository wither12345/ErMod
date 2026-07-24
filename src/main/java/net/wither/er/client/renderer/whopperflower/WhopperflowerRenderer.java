package net.wither.er.client.renderer.whopperflower;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;
import net.wither.er.client.models.WhopperflowerModel;
import net.wither.er.entity.whopperflower.Whopperflower;
import org.jetbrains.annotations.NotNull;

public abstract class WhopperflowerRenderer<T extends Whopperflower> extends MobRenderer<T, WhopperflowerModel<T>> {
    private final BlockRenderDispatcher dispatcher;

    public WhopperflowerRenderer(EntityRendererProvider.Context context) {
        super(context, new WhopperflowerModel<>(context.bakeLayer(WhopperflowerModel.LAYER_LOCATION)), 0.3f);
        this.addLayer(new WhopperflowerItemLayer<>(this, context.getItemRenderer()));
        dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(@NotNull T flower, float f, float dt, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int l) {
        if(flower.isDisguise()) {
            this.renderBlock(flower, poseStack, source);
            this.shadowRadius = 0 ;
            flower.dy = -3;
            poseStack.translate(0, flower.dy, 0);
        }
        else {
            this.shadowRadius = 0.3f;
            switch (flower.getAction()){
                case DOWN -> {
                    poseStack.translate(0, flower.dy, 0);
                    flower.dy = Math.max(flower.dy - dt / 20, -3);

                }
                case UP -> {
                    if(flower.dy < 0)
                        flower.dy = Math.min(0 , flower.dy + dt / 30);
                    poseStack.translate(0, flower.dy, 0);
                }
                default -> flower.dy = 0;
            }
            super.render(flower, f, dt, poseStack, source, l);
        }
    }

    protected void renderBlock(Whopperflower whopperflower, PoseStack poseStack, MultiBufferSource source){
        BlockState blockstate = whopperflower.getDisguiseBlockState();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = whopperflower.level();
            if (blockstate != level.getBlockState(whopperflower.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                poseStack.pushPose();
                BlockPos blockpos = BlockPos.containing(whopperflower.getX(), whopperflower.getBoundingBox().maxY, whopperflower.getZ());
                poseStack.translate(-0.5, 0.0F, -0.5);
                BakedModel model = this.dispatcher.getBlockModel(blockstate);

                for(RenderType renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(whopperflower.getOnPos())), ModelData.EMPTY)) {
                    this.dispatcher.getModelRenderer().tesselateBlock(level, this.dispatcher.getBlockModel(blockstate), blockstate, blockpos, poseStack, source.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType)), false, RandomSource.create(), blockstate.getSeed(whopperflower.getOnPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
                }

                poseStack.popPose();
            }
        }
    }
}
