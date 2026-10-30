package net.wither.er.mixins;

import net.mcreator.er.ErMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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

import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract DataComponentMap getComponents();

    @Inject(method = "parse", at = @At("HEAD"))
    private static void remapItemId(HolderLookup.Provider provider, Tag tag, CallbackInfoReturnable<Optional<ItemStack>> cir) {
        if (tag instanceof CompoundTag compound && compound.contains("id", Tag.TAG_STRING)) {
            String oldId = compound.getString("id");
            if ("er:memoryof_roving_gales".equals(oldId)) {
                ErMod.LOGGER.info("Remapped er:memoryof_roving_gales to er:memory_of_roving_gales");
                compound.putString("id", "er:memory_of_roving_gales");
            }
        }
    }

    @Inject(method = "getHoverName", at = @At("TAIL"))
    private void getDisplayName(CallbackInfoReturnable<Component> componentCallback){
        Component component = componentCallback.getReturnValue();
        ArtifactData artifactData = this.getComponents().get(DataComponentsRegister.ARTIFACT.get()) ;
        if(artifactData != null && component instanceof MutableComponent mutableComponent)
            mutableComponent.withColor(artifactData.getColor());
    }
}
