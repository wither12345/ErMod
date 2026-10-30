package net.wither.er.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.wither.er.item.ModifyTooltip;
import net.wither.er.item.data.FortunaData;
import net.wither.er.item.fortuna.StellaFortuna;
import net.wither.er.world.inventory.FortunaEnhanceMenu;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * 命星强化/突破、命座和技能界面。
 * <p>
 * 三个页签按钮放在 200x166 主界面右侧外面靠上的位置，强化页保持原来的布局不变。
 */
public class FortunaEnhanceGuiScreen extends AbstractContainerScreen<FortunaEnhanceMenu> {
    private static final ResourceLocation texture = new ResourceLocation("er", "textures/screens/fortuna_enhance_gui.png");
    private static final ResourceLocation bar = new ResourceLocation("er", "textures/screens/artifact_transmuter_bar.png");
    private static final ResourceLocation emptyFortuna = new ResourceLocation("er", "textures/item/empty_fortuna.png");
    private static final DecimalFormat VALUE_FORMAT = new DecimalFormat("##.#");

    private static final int TAB_ENHANCE = 0;
    private static final int TAB_CONSTELLATION = 1;
    private static final int TAB_SKILL = 2;

    private static final int BAR_X = 100;
    private static final int BAR_Y = 5;
    private static final int BAR_WIDTH = 81;
    private static final int TEXT_X = 96;

    private static final int CONSTELLATION_X = 100;
    private static final int CONSTELLATION_Y = 22;
    private static final int CONSTELLATION_GAP = 20;

    private int currentTab = TAB_ENHANCE;

    private Button enhanceTab;
    private Button constellationTab;
    private Button skillTab;

    private Button enhanceButton;
    private Button normalButton;
    private Button skillButton;
    private Button burstButton;

    public FortunaEnhanceGuiScreen(FortunaEnhanceMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.imageWidth = 200;
        this.imageHeight = 166;
    }

    @Override
    public void init() {
        super.init();

        // 页签放在主界面右外侧、偏上的位置，不占用原本的强化界面。
        int tabX = this.leftPos + this.imageWidth + 4;
        this.enhanceTab = Button.builder(Component.translatable("gui.er.fortuna_enhance.tab_enhance"),
                b -> this.selectTab(TAB_ENHANCE)).bounds(tabX, this.topPos + 8, 48, 18).build();
        this.constellationTab = Button.builder(Component.translatable("gui.er.fortuna_enhance.constellation"),
                b -> this.selectTab(TAB_CONSTELLATION)).bounds(tabX, this.topPos + 30, 48, 18).build();
        this.skillTab = Button.builder(Component.translatable("gui.er.fortuna_enhance.talents"),
                b -> this.selectTab(TAB_SKILL)).bounds(tabX, this.topPos + 52, 48, 18).build();
        this.addRenderableWidget(this.enhanceTab);
        this.addRenderableWidget(this.constellationTab);
        this.addRenderableWidget(this.skillTab);

        // 强化页按钮保持原来的位置。
        this.enhanceButton = Button.builder(Component.translatable("gui.er.fortuna_enhance.button_upgrade"),
                        b -> this.sendAction(0))
                .bounds(this.leftPos + 104, this.topPos + 66, 84, 14)
                .build();
        this.addRenderableWidget(this.enhanceButton);

        this.normalButton = Button.builder(Component.literal("+1"), b -> this.sendAction(2))
                .bounds(this.leftPos + 162, this.topPos + 22, 32, 18).build();
        this.skillButton = Button.builder(Component.literal("+1"), b -> this.sendAction(3))
                .bounds(this.leftPos + 162, this.topPos + 40, 32, 18).build();
        this.burstButton = Button.builder(Component.literal("+1"), b -> this.sendAction(4))
                .bounds(this.leftPos + 162, this.topPos + 58, 32, 18).build();
        this.addRenderableWidget(this.normalButton);
        this.addRenderableWidget(this.skillButton);
        this.addRenderableWidget(this.burstButton);

        this.updateWidgets();
    }

    private void sendAction(int id) {
        if (this.minecraft != null && this.minecraft.gameMode != null)
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
    }

    private void selectTab(int tab) {
        this.currentTab = tab;
        this.updateWidgets();
    }

    private void updateWidgets() {
        if (this.enhanceTab == null)
            return;

        // 当前页签显示为按下/不可再点，其余页签可点击。
        this.enhanceTab.active = this.currentTab != TAB_ENHANCE;
        this.constellationTab.active = this.currentTab != TAB_CONSTELLATION;
        this.skillTab.active = this.currentTab != TAB_SKILL;

        this.enhanceButton.visible = this.currentTab == TAB_ENHANCE;
        this.normalButton.visible = this.currentTab == TAB_SKILL;
        this.skillButton.visible = this.currentTab == TAB_SKILL;
        this.burstButton.visible = this.currentTab == TAB_SKILL;

        if (this.enhanceButton.visible) {
            this.enhanceButton.active = this.menu.canEnhance();
            this.enhanceButton.setMessage(switch (this.menu.getMode()) {
                case ASCEND -> Component.translatable("gui.er.fortuna_enhance.button_ascend");
                case MAX -> Component.translatable("gui.er.fortuna_enhance.button_max");
                default -> Component.translatable("gui.er.fortuna_enhance.button_upgrade");
            });
        }

        this.normalButton.active = this.menu.canUpgradeNormal();
        this.skillButton.active = this.menu.canUpgradeSkill();
        this.burstButton.active = this.menu.canUpgradeBurst();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.updateWidgets();
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        // 1 号槽位为空但使用了玩家装备的命星时，补上物品提示。
        if (this.menu.getSlot(1).getItem().isEmpty()
                && this.hoveredSlot == this.menu.getSlot(1)
                && this.menu.hasFortuna()) {
            ItemStack equipped = this.menu.getFortunaStack();
            if (!equipped.isEmpty())
                guiGraphics.renderTooltip(this.font, equipped, mouseX, mouseY);
        }

        if (this.currentTab == TAB_CONSTELLATION && this.menu.hasFortuna()) {
            int hovered = this.constellationHovered(mouseX, mouseY);
            if (hovered >= 0) {
                Component lore = this.constellationLore(this.menu.getFortunaStack(), hovered + 1);
                List<FormattedCharSequence> lines = this.font.split(lore, 140);
                guiGraphics.renderTooltip(this.font, lines, mouseX, mouseY);
            }
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.currentTab == TAB_CONSTELLATION && this.menu.hasFortuna()) {
            int hovered = this.constellationHovered(mouseX, mouseY);
            // 只有“下一个”命座可以点：它的前一个已经解锁。
            if (hovered >= 0
                    && hovered == this.menu.getConstellation()
                    && this.menu.canUpgradeConstellation()) {
                this.sendAction(1);
                if (this.minecraft != null)
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        if (this.menu.hasFortuna()) {
            switch (this.currentTab) {
                case TAB_ENHANCE -> this.renderEnhanceBar(guiGraphics);
                case TAB_CONSTELLATION -> this.renderConstellationNodes(guiGraphics, mouseX, mouseY);
                default -> {
                }
            }
        }

        this.renderEquippedFortuna(guiGraphics);
        RenderSystem.disableBlend();
    }

    private void renderEnhanceBar(GuiGraphics guiGraphics) {
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

    private void renderConstellationNodes(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        ItemStack stack = this.menu.getFortunaStack();
        int constellation = this.menu.getConstellation();
        int hovered = this.constellationHovered(mouseX, mouseY);

        for (int i = 0; i < FortunaEnhanceMenu.MAX_CONSTELLATION; i++) {
            int x = this.leftPos + CONSTELLATION_X + (i % 3) * CONSTELLATION_GAP;
            int y = this.topPos + CONSTELLATION_Y + (i / 3) * CONSTELLATION_GAP;

            if (i < constellation) {
                guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFFFFAA00);
                guiGraphics.renderItem(stack, x, y);
            } else {
                // 下一个可解锁的命座用金框，材料不足时用红框。
                if (i == constellation) {
                    int color = this.menu.canUpgradeConstellation() ? 0xFFFFAA00 : 0xFF5555;
                    guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, color);
                }
                guiGraphics.blit(emptyFortuna, x, y, 0, 0, 16, 16, 16, 16);
            }

            if (hovered == i)
                guiGraphics.fill(x, y, x + 16, y + 16, 0x30FFFFFF);
        }
    }

    private int constellationHovered(double mouseX, double mouseY) {
        for (int i = 0; i < FortunaEnhanceMenu.MAX_CONSTELLATION; i++) {
            int x = this.leftPos + CONSTELLATION_X + (i % 3) * CONSTELLATION_GAP;
            int y = this.topPos + CONSTELLATION_Y + (i / 3) * CONSTELLATION_GAP;
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16)
                return i;
        }
        return -1;
    }

    private Component constellationLore(ItemStack stack, int index) {
        if (!(stack.getItem() instanceof StellaFortuna))
            return Component.empty();

        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if(id != null) {
            String key = "lore.constellation." + id.getNamespace() + "." + id.getPath().replace('/', '.') + '.' + index;
            if (Language.getInstance().has(key))
                return Component.translatable(key);
        }
        return Component.translatable("gui.er.fortuna_enhance.constellation_lore", index);
    }

    private void renderEquippedFortuna(GuiGraphics guiGraphics) {
        if (!this.menu.getSlot(1).getItem().isEmpty())
            return;

        ItemStack equipped = this.menu.getFortunaStack();
        if (!(equipped.getItem() instanceof StellaFortuna))
            return;

        RenderSystem.setShaderColor(1, 1, 1, 0.5f);
        guiGraphics.renderItem(equipped,
                this.leftPos + this.menu.getSlot(1).x,
                this.topPos + this.menu.getSlot(1).y);
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (!this.menu.hasFortuna())
            return;

        switch (this.currentTab) {
            case TAB_ENHANCE -> this.renderEnhanceLabels(guiGraphics);
            case TAB_CONSTELLATION -> this.renderConstellationLabels(guiGraphics);
            case TAB_SKILL -> this.renderSkillLabels(guiGraphics);
            default -> {
            }
        }
    }

    private void renderEnhanceLabels(GuiGraphics guiGraphics) {
        int level = this.menu.getLevel();
        String head = String.valueOf(level);
        int gained = this.menu.getNewLevel() - level;
        if (gained > 0)
            head += "  +" + gained;
        int headWidth = this.font.width(head) / 2;
        this.renderExpLikeString(guiGraphics, head, BAR_X + BAR_WIDTH / 2 - headWidth);

        int maxLevel = FortunaData.getMaxLevel(this.menu.getAscension());
        if (level >= maxLevel) {
            guiGraphics.drawString(this.font, "MAX", TEXT_X + 1, 12, 0x886600, false);
            guiGraphics.drawString(this.font, "MAX", TEXT_X, 11, 0xFF9900, false);
        } else {
            guiGraphics.drawString(this.font, this.menu.getExperience() + "/" + FortunaData.getExpReq(level),
                    TEXT_X, 12, 0xFFFFFF, false);
        }

        StellaFortuna fortuna = (StellaFortuna) this.menu.getFortunaStack().getItem();
        float oldScale = StellaFortuna.levelScaling(level, fortuna.levelType, this.menu.getAscension());
        float newScale = StellaFortuna.levelScaling(this.menu.getNewLevel(), fortuna.levelType, this.menu.getNewAscension());
        guiGraphics.drawString(this.font, ModifyTooltip.getAscension(this.menu.getNewAscension(), 5), TEXT_X, 21, 0xFFFFFF, false);
        this.drawStat(guiGraphics, "text.er.fortuna_health", fortuna.health * oldScale, fortuna.health * newScale, 30);
        this.drawStat(guiGraphics, "text.er.fortuna_attack", fortuna.damage * oldScale, fortuna.damage * newScale, 39);
        this.drawStat(guiGraphics, "text.er.fortuna_armor", fortuna.armor * oldScale, fortuna.armor * newScale, 48);

        guiGraphics.drawString(this.font, this.statusText(), TEXT_X, 58, this.statusColor(), false);
    }

    private void renderConstellationLabels(GuiGraphics guiGraphics) {
        int constellation = this.menu.getConstellation();
        guiGraphics.drawString(this.font,
                Component.translatable("gui.er.fortuna_enhance.constellation",
                        constellation, FortunaEnhanceMenu.MAX_CONSTELLATION).getString(),
                TEXT_X, 6, 0xFFFFFF, false);

        if (constellation < FortunaEnhanceMenu.MAX_CONSTELLATION) {
            boolean canRaise = this.menu.canUpgradeConstellation();
            String hint = getHint(canRaise);
            guiGraphics.drawString(this.font, hint, 160, 22, canRaise ? 0xFFAA00 : 0xFF5555, false);
        }

        ItemStack stack = this.menu.getFortunaStack();
        int next = constellation + 1;
        if (next > FortunaEnhanceMenu.MAX_CONSTELLATION) {
            guiGraphics.drawString(this.font,
                    Component.translatable("gui.er.fortuna_enhance.status_constellation_max").getString(),
                    TEXT_X, 68, 0xAAAAAA, false);
            return;
        }

        // 底部预览下一命座的描述；悬停任意命座节点时用 Tooltip 显示完整描述。
        List<FormattedCharSequence> lines = this.font.split(this.constellationLore(stack, next), 100);
        int y = 64;
        for (int i = 0; i < Math.min(2, lines.size()); i++) {
            guiGraphics.drawString(this.font, lines.get(i), TEXT_X, y, 0xAAAAAA, false);
            y += 9;
        }
    }

    private @NotNull String getHint(boolean canRaise) {
        boolean empty = this.menu.isEmptyFortuna();
        if (canRaise)
            return empty
                    ? Component.translatable("gui.er.fortuna_enhance.hint_constellation_transform").getString()
                    : Component.translatable("gui.er.fortuna_enhance.hint_constellation_click").getString();
         return empty
                 ? Component.translatable("gui.er.fortuna_enhance.button_constellation_need_sacrifice").getString()
                 : Component.translatable("gui.er.fortuna_enhance.button_constellation_need").getString();
    }

    private void renderSkillLabels(GuiGraphics guiGraphics) {
        guiGraphics.drawString(this.font,
                Component.translatable("gui.er.fortuna_enhance.talents").getString(),
                TEXT_X, 6, 0xFFFFFF, false);

        this.drawSkillRow(guiGraphics, "gui.er.fortuna_skill.normal", this.menu.getNormalLevel(), this.menu.canUpgradeNormal(), 26);
        this.drawSkillRow(guiGraphics, "gui.er.fortuna_skill.skill", this.menu.getSkillLevel(), this.menu.canUpgradeSkill(), 44);
        this.drawSkillRow(guiGraphics, "gui.er.fortuna_skill.burst", this.menu.getBurstLevel(), this.menu.canUpgradeBurst(), 62);
    }

    private void drawSkillRow(GuiGraphics guiGraphics, String translationKey, int level, boolean canUpgrade, int y) {
        String text = Component.translatable(translationKey).getString()
                + " " + level + "/" + FortunaEnhanceMenu.MAX_SKILL_LEVEL;
        int color;
        if (level >= FortunaEnhanceMenu.MAX_SKILL_LEVEL)
            color = 0xFFAA00;
        else if (!canUpgrade)
            color = 0xFF5555;
        else
            color = 0xFFFFFF;
        guiGraphics.drawString(this.font, text, TEXT_X, y, color, false);
    }

    private void drawStat(GuiGraphics guiGraphics, String translationKey, float before, float after, int y) {
        String text = Component.translatable(translationKey).getString() + " " + format(before) + ">" + format(after);
        int color = after > before ? 0x55FF55 : 0xFFFFFF;
        guiGraphics.drawString(this.font, text, TEXT_X, y, color, false);
    }

    private void renderExpLikeString(@NotNull GuiGraphics guiGraphics, String s, int x) {
        guiGraphics.drawString(this.font, s, x + 1, 3, 0, false);
        guiGraphics.drawString(this.font, s, x - 1, 3, 0, false);
        guiGraphics.drawString(this.font, s, x, 4, 0, false);
        guiGraphics.drawString(this.font, s, x, 2, 0, false);
        guiGraphics.drawString(this.font, s, x, 3, 8453920, false);
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
    protected void containerTick() {
        super.containerTick();
        // 玩家装备数据同步后立即刷新预览。
        this.menu.refreshPreview();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }
}