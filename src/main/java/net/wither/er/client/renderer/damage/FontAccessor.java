package net.wither.er.client.renderer.damage;

import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.ResourceLocation;

public interface FontAccessor {
    boolean er$getFilterFishyGlyphs();
    FontSet er$callGetFontSet(ResourceLocation location);
}
