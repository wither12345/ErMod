package net.wither.er.mixins;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "getHoverName", at = @At("TAIL"))
    private void getDisplayName(CallbackInfoReturnable<Component> componentCallback){
        Component component = componentCallback.getReturnValue();
        ArtifactData artifactData = DataComponentsRegister.ARTIFACT.getData((ItemStack)(Object)this) ;
        if(artifactData != null && component instanceof MutableComponent mutableComponent)
            mutableComponent.withStyle(mutableComponent.getStyle().withColor(artifactData.getColor()));
    }
}
