package net.wither.er.mixins;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract DataComponentMap getComponents();

    @Inject(method = "getHoverName", at = @At("TAIL"))
    private void getDisplayName(CallbackInfoReturnable<Component> componentCallback){
        Component component = componentCallback.getReturnValue();
        ArtifactData artifactData = this.getComponents().get(DataComponentsRegister.ARTIFACT.get()) ;
        if(artifactData != null && component instanceof MutableComponent mutableComponent)
            mutableComponent.withColor(artifactData.getColor());
    }
}
