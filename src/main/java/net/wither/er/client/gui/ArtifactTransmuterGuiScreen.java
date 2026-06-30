package net.wither.er.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.artifactdata.MinorAffix;
import net.wither.er.network.ArtifactTransmuterMessage;
import net.wither.er.world.inventory.ArtifactTransmuterGuiMenu;
import org.jetbrains.annotations.NotNull;

public class ArtifactTransmuterGuiScreen extends AbstractContainerScreen<ArtifactTransmuterGuiMenu>  {
    Button button_enhance;

    public ArtifactTransmuterGuiScreen(ArtifactTransmuterGuiMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.imageWidth = 200;
        this.imageHeight = 166;
    }

    private static final ResourceLocation texture = ResourceLocation.parse("er:textures/screens/artifact_transmuter_gui.png");
    private static final ResourceLocation bar = ResourceLocation.parse("er:textures/screens/artifact_transmuter_bar.png");

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        ArtifactData data = menu.getSlot(1).getItem().get(DataComponentsRegister.ARTIFACT.get()) ;
        if (data != null) {

            RenderSystem.setShaderColor(1, 1, 1, 1);
            guiGraphics.blit(bar, this.leftPos + 100, this.topPos + 14, 0, 0, 81, 5, 81, 10);
            guiGraphics.blit(bar, this.leftPos + 100, this.topPos + 14, 0, 5, ArtifactData.experience_percentage(81, data.level(), data.rarity()), 5, 81, 10);
            RenderSystem.setShaderColor(1, 1, 1, 0.5f);
            if(menu.getNewLv() > data.level().level())
                guiGraphics.blit(bar, this.leftPos + 100, this.topPos + 14, 0, 5, 81, 5, 81, 10);
            else
                guiGraphics.blit(bar, this.leftPos + 100, this.topPos + 14, 0, 5, ArtifactData.experience_percentage(81, menu.getNewLv(), menu.getNewExp(), data.rarity()), 5, 81, 10);
        }
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        ArtifactData data = menu.getSlot(1).getItem().get(DataComponentsRegister.ARTIFACT.get()) ;
        if(data != null) {
            String s = String.valueOf(data.level().level()) ;
            int add = getMenu().getNewLv() - data.level().level();
            if(add > 0)
                s += "  +" + add ;
            int w = this.font.width(s) / 2 ;
            this.renderExpLikeString(guiGraphics, s, 136 - w, 10) ;

            int y0 = 18 ;

            s = data.main().toString(data.level().level(), data.rarity());
            if(menu.getNewLv() > data.level().level()) {
                String format = "##.#";
                if(data.main().attribute() == ErModAttributes.CRIT_RATE.getDelegate() || data.main().attribute() == ErModAttributes.CRIT_DAMAGE.getDelegate() || data.main().multi())
                    format += "%" ;

                s += " > " + new java.text.DecimalFormat(format).format(data.main().calculate(menu.getNewLv(), data.rarity()));
            }
            guiGraphics.drawString(this.font, s, 100, y0 += 8, 0xFFFFFF, false);

            int minor_upgrade = menu.getMinorUpgrade();
            if(minor_upgrade > 0) {
                int minor_count = data.minor().size();
                if (minor_count < 4) {
                    int c = Math.min(minor_upgrade, 4 - minor_count) ;
                    s = new java.text.DecimalFormat(Component.translatable("text.er.minor_affix_add").getString()).format(c);
                    guiGraphics.drawString(this.font, s, 100, y0 += 8, 0xFFFF00, false);

                    minor_upgrade -= c ;
                }
                if(minor_upgrade > 0){
                    s = new java.text.DecimalFormat(Component.translatable("text.er.minor_affix_upgrade").getString()).format(minor_upgrade);
                    guiGraphics.drawString(this.font, s, 100, y0 += 8, 0xFFFF00, false);
                }
            }

            for(MinorAffix affix : data.minor()){
                guiGraphics.drawString(this.font, affix.toString(data.rarity()), 108, y0 += 8, 0xEEEEEE, false);
            }
        }
    }

    private void renderExpLikeString(@NotNull GuiGraphics guiGraphics, String s, int x, int y){
        guiGraphics.drawString(this.font, s, x + 1, y, 0, false);
        guiGraphics.drawString(this.font, s, x - 1, y, 0, false);
        guiGraphics.drawString(this.font, s, x, y + 1, 0, false);
        guiGraphics.drawString(this.font, s, x, y - 1, 0, false);
        guiGraphics.drawString(this.font, s, x, y, 8453920, false);
    }

    @Override
    public void init() {
        super.init();
        button_enhance = Button.builder(Component.translatable("gui.er.artifact_transmuter_gui.button_enhance"), e -> {
            PacketDistributor.sendToServer(new ArtifactTransmuterMessage());
        }).bounds(this.leftPos + 24, this.topPos + 61, 54, 20).build();
        this.addRenderableWidget(button_enhance);
    }
}