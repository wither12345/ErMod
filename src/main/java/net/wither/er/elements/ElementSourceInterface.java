package net.wither.er.elements;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface ElementSourceInterface {
    @Nullable ElementSource er$getSource();

    Object er$setElement(@Nullable ElementSource source);

    default Object er$setElement(@NotNull Element element, ResourceLocation resourceLocation, float gauge){
        return er$setElement(new ElementSource(element,resourceLocation,gauge, element.isApplicable()));
    }
}
