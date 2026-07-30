package net.wither.er.elements;

import net.minecraft.resources.ResourceLocation;

public interface ElementSourceInterface {
    ElementSource er$getSource();

    Object er$setElement(ElementSource source);

    default Object er$setElement(Element element, ResourceLocation resourceLocation, float gauge){
        return er$setElement(new ElementSource(element,resourceLocation,gauge, element.isApplicable()));
    }
}
