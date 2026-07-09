package net.wither.er.mixins;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.client.renderer.damage.FontAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Font.class)
public abstract class FontMixin implements FontAccessor {
    @Shadow @Final
    boolean filterFishyGlyphs;
    @Shadow abstract FontSet getFontSet(ResourceLocation p_92864_);

    @Override
    public boolean er$getFilterFishyGlyphs(){
        return filterFishyGlyphs;
    }

    @Override
    public FontSet er$callGetFontSet(ResourceLocation location){
        return getFontSet(location);
    }
}
