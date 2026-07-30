package net.wither.er.item.morabag;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.wither.er.player.ErPlayerInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClientMoraBagTooltip implements ClientTooltipComponent {
    //private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("container/bundle/background");
    private final List<MoraBagItemPlus.MoraVal> moraVals;
    public ClientMoraBagTooltip(MoraBagComponent component){
        this.moraVals = component.getVals();
    }

    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        //guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int i1 = 0; i1 < i; ++i1) {
                int j1 = x + i1 * 18 + 1;
                int k1 = y + l * 20 + 1;
                this.renderSlot(j1, k1, k++, guiGraphics, font);
            }
        }
    }

    private void renderSlot(int x, int y, int index, GuiGraphics guiGraphics, Font font) {
        if (index < this.moraVals.size()) {
            ItemStack itemstack = this.moraVals.get(index).item().getDefaultInstance();
            //this.blit(guiGraphics, x, y, Texture.SLOT);
            guiGraphics.renderItem(itemstack, x + 1, y + 1, index);
            guiGraphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
            ErPlayerInterface playerInterface = (ErPlayerInterface) Minecraft.getInstance().player;
            if (playerInterface != null && index == playerInterface.er$getMoraIndex()) {
                AbstractContainerScreen.renderSlotHighlight(guiGraphics, x + 1, y + 1, 0);
            }
        }
    }

    public int getHeight() {
        return this.backgroundHeight() + 4;
    }

    public int getWidth(@NotNull Font font) {
        return this.backgroundWidth();
    }

    private int backgroundWidth() {
        return this.gridSizeX() * 18 + 2;
    }

    private int backgroundHeight() {
        return this.gridSizeY() * 20 + 2;
    }

    private int gridSizeX() {
        return Math.max(2, (int)Math.ceil(Math.sqrt((double)this.moraVals.size() + (double)1.0F)));
    }

    private int gridSizeY() {
        return (int)Math.ceil(((double)this.moraVals.size() + (double)1.0F) / (double)this.gridSizeX());
    }

    @OnlyIn(Dist.CLIENT)
    enum Texture {
        BLOCKED_SLOT(new ResourceLocation("container/bundle/blocked_slot"), 18, 20),
        SLOT(new ResourceLocation("container/bundle/slot"), 18, 20);

        public final ResourceLocation sprite;
        public final int w;
        public final int h;

        private Texture(ResourceLocation p_295000_, int p_169928_, int p_169929_) {
            this.sprite = p_295000_;
            this.w = p_169928_;
            this.h = p_169929_;
        }
    }
}
