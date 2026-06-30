package net.wither.er.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.network.AlchemyConvertingSwitchMessage;
import net.wither.er.network.AlchemyStageSwitchMessage;
import net.wither.er.world.inventory.AlchemyGuiMenu;
import org.jetbrains.annotations.NotNull;

public class AlchemyGuiScreen extends AbstractContainerScreen<AlchemyGuiMenu> {
    Button buttonConvertingNext;
    Button buttonConvertingPre;
    Button buttonCrafting;
    Button buttonConverting;


    public AlchemyGuiScreen(AlchemyGuiMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    private static final ResourceLocation texture = ResourceLocation.parse("er:textures/screens/alchemy_craft_gui.png");
    private static final ResourceLocation slot_texture = ResourceLocation.parse("er:textures/screens/slot.png");

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        if(menu.getStage() == AlchemyGuiMenu.Stage.CRAFTING){
            blitSlot(guiGraphics, 33, 16);
            blitSlot(guiGraphics, 33, 52);
        }
        else {
            blitSlot(guiGraphics, 78, 34);
            blitSlot(guiGraphics, 123, 16);
            blitSlot(guiGraphics, 123, 52);
        }
        RenderSystem.disableBlend();
    }

    private void blitSlot(GuiGraphics guiGraphics, int x, int y){
        guiGraphics.blit(slot_texture, this.leftPos + x, this.topPos + y, 0, 0, 18, 18, 18, 18);
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.closeContainer();
            }
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    public void init() {
        super.init();
        buttonConvertingPre = Button.builder(Component.literal("<"), e -> {
            PacketDistributor.sendToServer(new AlchemyConvertingSwitchMessage(false));
        }).bounds(this.leftPos + 140, this.topPos + 34, 8, 8).build();
        buttonConvertingNext = Button.builder(Component.literal(">"), e -> {
            PacketDistributor.sendToServer(new AlchemyConvertingSwitchMessage(true));
        }).bounds(this.leftPos + 140, this.topPos + 42, 8, 8).build();
        buttonCrafting = Button.builder(Component.translatable("gui.er.crafting"), e -> {
            this.switchStage(AlchemyGuiMenu.Stage.CRAFTING);
        }).bounds(this.leftPos + 176, this.topPos, 32, 20).build();
        buttonConverting = Button.builder(Component.translatable("gui.er.converting"), e -> {
            this.switchStage(AlchemyGuiMenu.Stage.CONVERTING);
        }).bounds(this.leftPos + 176, this.topPos + 18, 32, 20).build();

        this.buttonConvertingPre.visible = false ;
        this.buttonConvertingNext.visible = false ;
        this.addRenderableWidget(buttonConvertingPre);
        this.addRenderableWidget(buttonConvertingNext);
        this.addRenderableWidget(buttonCrafting);
        this.addRenderableWidget(buttonConverting);
    }

    private void switchStage(AlchemyGuiMenu.Stage stage){
        this.buttonConvertingPre.visible = (stage == AlchemyGuiMenu.Stage.CONVERTING);
        this.buttonConvertingNext.visible = (stage == AlchemyGuiMenu.Stage.CONVERTING);
        this.menu.switchStage(stage);
        PacketDistributor.sendToServer(new AlchemyStageSwitchMessage(stage));
    }
}
