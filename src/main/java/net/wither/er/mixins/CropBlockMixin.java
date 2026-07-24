package net.wither.er.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.wither.er.block.ElementalFarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class CropBlockMixin {
    @Inject(method = "getGrowthSpeed", at = @At("TAIL"), cancellable = true)
    private static void getGrowthSpeed(Block block, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        float f = 0 ;
        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                BlockState blockstate = blockGetter.getBlockState(blockPos.offset(i, 0, j));
                if(blockstate.getBlock() instanceof ElementalFarmBlock elementalFarmBlock)
                    f += ((i == 0 && j == 0) ? 1 : 0.25f) * elementalFarmBlock.getFertile(blockstate) ;
            }
        }
        cir.setReturnValue(cir.getReturnValueF() + f);
    }
}
