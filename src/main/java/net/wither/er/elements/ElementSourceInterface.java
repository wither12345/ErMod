package net.wither.er.elements;

import net.minecraft.resources.ResourceLocation;

public interface ElementSourceInterface {
    ElementSource getSource();

    Object setElement(ElementSource source);

    default Object setElement(Element element, ResourceLocation resourceLocation, float gauge){
        return setElement(new ElementSource(element,resourceLocation,gauge, true));
    }
}
