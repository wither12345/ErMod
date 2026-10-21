package net.wither.er.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.wither.er.item.StellaFortuna;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.wither.er.item.ModifyTooltip;
import net.wither.er.item.data.FortunaData;
import net.wither.er.world.inventory.FortunaEnhanceMenu;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Locale;

public class FortunaEnhanceGuiScreen extends AbstractContainerScreen<FortunaEnhanceMenu> {
    private static final ResourceLocation texture = ResourceLocation.parse("er:textures/screens/fortuna_enhance_gui.png");
    private static final ResourceLocation bar = ResourceLocation.parse("er:textures/screens/artifact_transmuter_bar.png");
    private static final DecimalFormat VALUE_FORMAT = new DecimalFormat("##.#");

    private static final int BAR_X = 100;
    private static final int BAR_Y = 5;
    private static final int BAR_WIDTH = 81;
    private static final int TEXT_X = 96;

    private Button enhanceButton;

    public FortunaEnhanceGuiScreen(FortunaEnhanceMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.imageWidth = 200;
        this.imageHeight = 166;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.updateButton();
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        if (this.menu.hasFortuna()) {
            int x = this.leftPos + BAR_X;
            int y = this.topPos + BAR_Y;
            guiGraphics.blit(bar, x, y, 0, 0, BAR_WIDTH, 5, BAR_WIDTH, 10);
            guiGraphics.blit(bar, x, y, 0, 5,
                    expPercentage(this.menu.getLevel(), this.menu.getExperience(), this.menu.getAscension()),
                    5, BAR_WIDTH, 10);

            RenderSystem.setShaderColor(1, 1, 1, 0.5f);
            if (this.menu.getNewLevel() > this.menu.getLevel())
                guiGraphics.blit(bar, x, y, 0, 5, BAR_WIDTH, 5, BAR_WIDTH, 10);
            else
                guiGraphics.blit(bar, x, y, 0, 5,
                        expPercentage(this.menu.getNewLevel(), this.menu.getNewExperience(), this.menu.getNewAscension()),
                        5, BAR_WIDTH, 10);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }

        RenderSystem.disableBlend();
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (!this.menu.hasFortuna())
            return;

        int level = this.menu.getLevel();
        String head = String.valueOf(level);

        int gained = this.menu.getNewLevel() - level;
        if (gained > 0)
            head += " + " + gained;

        int headWidth = this.font.width(head) / 2;
        this.renderExpLikeString(guiGraphics, head, BAR_X + BAR_WIDTH / 2 - headWidth);

        int maxLevel = FortunaData.getMaxLevel(this.menu.getAscension());
        if(level >= maxLevel) {
            guiGraphics.drawString(this.font, "MAX", TEXT_X + 1, 12, 0x886600, false);
            guiGraphics.drawString(this.font, "MAX", TEXT_X, 11, 0xFF9900, false);
        }
        else
            guiGraphics.drawString(this.font, this.menu.getExperience() + "/" + FortunaData.getExpReq(level), TEXT_X, 12, 0xFFFFFF, false);

        StellaFortuna fortuna = (StellaFortuna) this.menu.getSlot(1).getItem().getItem();
        float oldScale = StellaFortuna.levelScaling(level, fortuna.levelType, this.menu.getAscension());
        float newScale = StellaFortuna.levelScaling(this.menu.getNewLevel(), fortuna.levelType, this.menu.getNewAscension());
        guiGraphics.drawString(this.font, ModifyTooltip.getAscension(this.menu.getNewAscension(), 5), TEXT_X, 21, 0xFFFFFF);
        this.drawStat(guiGraphics, "text.er.fortuna_health", fortuna.health * oldScale, fortuna.health * newScale, 30);
        this.drawStat(guiGraphics, "text.er.fortuna_attack", fortuna.damage * oldScale, fortuna.damage * newScale, 39);
        this.drawStat(guiGraphics, "text.er.fortuna_armor", fortuna.armor * oldScale, fortuna.armor * newScale, 48);

        guiGraphics.drawString(this.font, this.statusText(), TEXT_X, 58, this.statusColor(), false);
    }

    private void drawStat(GuiGraphics guiGraphics, String translationKey, float before, float after, int y) {
        String text = Component.translatable(translationKey).getString() + " " + format(before) + ">" + format(after);
        int color = after > before ? 0x55FF55 : 0xFFFFFF;
        guiGraphics.drawString(this.font, text, TEXT_X, y, color, false);
    }

    private String statusText() {
        return switch (this.menu.getMode()) {
            case UPGRADE -> {
                if (this.menu.getExpGain() > 0) {
                    yield Component.translatable("gui.er.fortuna_enhance.status_upgrade",
                            shortNumber(this.menu.getExpGain()), shortNumber(this.menu.getMoraUse())).getString();
                }
                if (this.menu.hasExpMaterial())
                    yield Component.translatable("gui.er.fortuna_enhance.status_missing").getString();
                yield Component.translatable("gui.er.fortuna_enhance.status_need_exp").getString();
            }
            case ASCEND -> this.menu.canEnhance()
                    ? Component.translatable("gui.er.fortuna_enhance.status_ascend",
                        FortunaData.getMaxLevel(this.menu.getNewAscension())).getString()
                    : Component.translatable("gui.er.fortuna_enhance.status_missing").getString();
            case MAX -> Component.translatable("gui.er.fortuna_enhance.status_max").getString();
            default -> "";
        };
    }

    private int statusColor() {
        return switch (this.menu.getMode()) {
            case UPGRADE -> this.menu.canEnhance() ? 0x55FF55 : (this.menu.hasExpMaterial() ? 0xFF5555 : 0xAAAAAA);
            case ASCEND -> this.menu.canEnhance() ? 0xFFAA00 : 0xFF5555;
            case MAX, NONE -> 0xAAAAAA;
        };
    }

    private void updateButton() {
        if (this.enhanceButton == null)
            return;

        this.enhanceButton.active = this.menu.canEnhance();
        this.enhanceButton.setMessage(switch (this.menu.getMode()) {
            case ASCEND -> Component.translatable("gui.er.fortuna_enhance.button_ascend");
            case MAX -> Component.translatable("gui.er.fortuna_enhance.button_max");
            default -> Component.translatable("gui.er.fortuna_enhance.button_upgrade");
        });
    }

    private void renderExpLikeString(@NotNull GuiGraphics guiGraphics, String s, int x) {
        guiGraphics.drawString(this.font, s, x + 1, 3, 0, false);
        guiGraphics.drawString(this.font, s, x - 1, 3, 0, false);
        guiGraphics.drawString(this.font, s, x, 4, 0, false);
        guiGraphics.drawString(this.font, s, x, 2, 0, false);
        guiGraphics.drawString(this.font, s, x, 3, 8453920, false);
    }

    private static int expPercentage(int level, int experience, int ascension) {
        if (level <= 0)
            return 0;

        if (level >= FortunaData.getMaxLevel(ascension))
            return BAR_WIDTH;

        int required = FortunaData.getExpReq(level);
        if (required <= 0)
            return 0;

        return Math.max(0, Math.min(BAR_WIDTH, BAR_WIDTH * experience / required));
    }

    private static String format(float value) {
        return VALUE_FORMAT.format(value);
    }

    private static String shortNumber(int value) {
        if (value >= 1_000_000)
            return String.format(Locale.ROOT, "%.1fM", value / 1_000_000.0);
        if (value >= 10_000)
            return String.format(Locale.ROOT, "%.1fk", value / 1_000.0);
        return String.valueOf(value);
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
    public void init() {
        super.init();
        this.enhanceButton = Button.builder(Component.translatable("gui.er.fortuna_enhance.button_upgrade"), button -> {
            if (this.minecraft != null && this.minecraft.gameMode != null)
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
        }).bounds(this.leftPos + 104, this.topPos + 66, 84, 14).build();
        this.addRenderableWidget(this.enhanceButton);
    }
}
