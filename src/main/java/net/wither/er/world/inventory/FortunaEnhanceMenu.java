package net.wither.er.world.inventory;

import net.wither.er.item.data.SkillData;
import net.wither.er.item.fortuna.StellaFortuna;
import net.mcreator.er.init.ErModItems;
import net.mcreator.er.item.ExperienceBook;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.init.ErMenus;
import net.wither.er.item.data.FortunaData;
import net.wither.er.network.ErItemVariables;
import net.wither.er.recipe.ascension.AscensionRecipe;
import net.wither.er.recipe.ascension.AscensionRecipeListener;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * 命星（Fortuna）强化/突破、命座与技能菜单。
 * <p>
 * 0 号槽位放摩拉袋，1 号槽位放需要强化的命星，2~6 号槽位放经验书或突破/技能材料。
 * 与武器强化不同，这里不生成输出物品，预览和最终结果都直接写回玩家装备或 1 号槽位的物品。
 */
public class FortunaEnhanceMenu extends AbstractContainerMenu {
    public enum Mode {
        /** 没有放入可强化的命星。 */
        NONE,
        /** 未达到当前突破等级上限，按钮执行升级。 */
        UPGRADE,
        /** 已达到当前等级上限，按钮执行突破。 */
        ASCEND,
        /** 已达到最终等级上限，无法继续强化。 */
        MAX
    }

    /** 服务端突破判定：有配方，但材料或摩拉不足。 */
    private static final int ASCEND_STATE_MISSING = 1;
    /** 服务端突破判定：可以突破。 */
    private static final int ASCEND_STATE_READY = 2;
    /** 服务端突破判定：没有下一阶突破配方。 */
    private static final int ASCEND_STATE_MAX = 3;

    /** 命座上限。 */
    public static final int MAX_CONSTELLATION = 6;
    /** 普攻/战技/爆发的等级上限。 */
    public static final int MAX_SKILL_LEVEL = 10;

    /** 可作为“牺牲命座”的旅行者命星。 */
    public static final TagKey<Item> TRAVELER_FORTUNA_TAG = ItemTags.create(new ResourceLocation("er", "traveler_fortuna"));

    public final Level world;
    public final Player entity;

    private final Container container = new TransientCraftingContainer(this, 7, 1);
    /** 0 = 突破状态，1 = 突破后的阶数。突破配方只在服务端加载，因此需要同步给客户端。 */
    private final SimpleContainerData ascensionPreview = new SimpleContainerData(2);
    private final int[] shrink = new int[5];

    private int level;
    private int experience;
    private int ascension;
    private int newLevel;
    private int newExperience;
    private int newAscension;
    private int expGain;
    private int moraUse;
    private boolean hasExpMaterial;
    private boolean canEnhance;
    private Mode mode = Mode.NONE;

    private int constellation;
    private int normalLevel;
    private int skillLevel;
    private int burstLevel;
    private boolean canUpgradeConstellation;
    private boolean canUpgradeNormal;
    private boolean canUpgradeSkill;
    private boolean canUpgradeBurst;

    public FortunaEnhanceMenu(int id, Inventory inv) {
        super(ErMenus.FORTUNA_ENHANCE.get(), id);
        this.entity = inv.player;
        this.world = inv.player.level();

        this.addSlot(new MoraSlot(container, 0, 44, 8));
        this.addSlot(new Slot(container, 1, 44, 35) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof StellaFortuna;
            }
        });
        for (int i = 0; i < 5; i++)
            this.addSlot(new Slot(container, 2 + i, 7 + 18 * i, 62));

        for (int si = 0; si < 3; ++si)
            for (int sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (int si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 8 + si * 18, 142));

        this.addDataSlots(ascensionPreview);
        this.predict();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();
            int startSlot = 2;
            if(slotItem.getItem() != this.getFortunaStack().getItem() && !this.getFortunaStack().is(ErModItems.EMPTY_FORTUNA.get()))
                startSlot = 1;
            if(slotItem.is(ErModItems.MORA_BAG.get()))
                startSlot = 0;
            if (index < 7) {
                if (!this.moveItemStackTo(slotItem, 7, this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(slotItem, itemstack);
            } else if (!this.moveItemStackTo(slotItem, startSlot, 7, false)) {
                if (index < 7 + 27) {
                    if (!this.moveItemStackTo(slotItem, 7 + 27, this.slots.size(), true))
                        return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(slotItem, 7, 7 + 27, false))
                        return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            }
            if (slotItem.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (slotItem.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, slotItem);
        }
        return itemstack;
    }

    @Override
    public void removed(@NotNull Player playerIn) {
        super.removed(playerIn);
        if (playerIn instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
                for (int j = 0; j < 7; ++j) {
                    playerIn.drop(container.getItem(j), false);
                }
            } else {
                for (int i = 0; i < 7; ++i) {
                    playerIn.getInventory().placeItemBackInInventory(container.getItem(i));
                }
            }
        }
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        this.predict();
        super.slotsChanged(container);
    }

    @Override
    public void broadcastChanges() {
        this.predict();
        super.broadcastChanges();
    }

    @Override
    public void setData(int id, int value) {
        super.setData(id, value);
        // 客户端收到服务端的突破判定后立刻重新计算预览。
        this.predict();
    }

    @Override
    public boolean clickMenuButton(@NotNull Player player, int id) {
        if (!(player instanceof ServerPlayer))
            return false;

        switch (id) {
            case 0 -> this.enhance();
            case 1 -> this.upgradeConstellation();
            case 2 -> this.upgradeSkill(0);
            case 3 -> this.upgradeSkill(1);
            case 4 -> this.upgradeSkill(2);
            default -> {
                return false;
            }
        }
        return true;
    }

    private void predict() {
        this.mode = Mode.NONE;
        this.canEnhance = false;
        this.expGain = 0;
        this.moraUse = 0;
        this.hasExpMaterial = false;
        Arrays.fill(this.shrink, 0);

        ItemStack stack = this.getFortunaStack();
        if (!(stack.getItem() instanceof StellaFortuna)) {
            this.level = 0;
            this.experience = 0;
            this.ascension = 0;
            this.newLevel = 0;
            this.newExperience = 0;
            this.newAscension = 0;
            this.constellation = 0;
            this.normalLevel = 0;
            this.skillLevel = 0;
            this.burstLevel = 0;
            this.canUpgradeConstellation = false;
            this.canUpgradeNormal = false;
            this.canUpgradeSkill = false;
            this.canUpgradeBurst = false;
            return;
        }

        // 物品中保存的 level 从 0 开始，展示和等级需求都使用从 1 开始的等级。
        FortunaData data = FortunaData.get(stack);
        this.level = data.level();
        this.experience = data.experience();
        this.ascension = data.ascension();
        this.newLevel = this.level;
        this.newExperience = this.experience;
        this.newAscension = this.ascension;

        this.constellation = data.constellation();
        this.normalLevel = data.skills().normal();
        this.skillLevel = data.skills().skill();
        this.burstLevel = data.skills().burst();
        // 命座需要消耗一个同种命星；技能升级读取 getFortunaSkill 对应的配方。
        this.canUpgradeConstellation = this.constellation < MAX_CONSTELLATION && this.findConstellationMaterial() >= 0;
        this.canUpgradeNormal = this.normalLevel < data.getMaxTalentLevel() && this.canUseSkillRecipe(stack, this.normalLevel);
        this.canUpgradeSkill = this.skillLevel < data.getMaxTalentLevel() && this.canUseSkillRecipe(stack, this.skillLevel);
        this.canUpgradeBurst = this.burstLevel < data.getMaxTalentLevel() && this.canUseSkillRecipe(stack, this.burstLevel);

        int maxLevel = FortunaData.getMaxLevel(this.ascension);
        if (this.level < maxLevel) {
            this.predictUpgrade(maxLevel);
        } else {
            this.predictAscension(stack);
        }
    }

    /** 返回 2~6 号槽中第一个与目标命星同种的槽位下标，没有则返回 -1。 */
    private int findConstellationMaterial() {
        ItemStack target = this.getFortunaStack();
        if (!(target.getItem() instanceof StellaFortuna))
            return -1;

        boolean empty = target.is(ErModItems.EMPTY_FORTUNA.get());

        for (int i = 0; i < this.shrink.length; i++) {
            ItemStack material = this.getSlot(2 + i).getItem();
            if (material.isEmpty())
                continue;
            // 空命星需要旅行者命星作为“牺牲命座”来转化；普通命星消耗同种命星。
            if (empty
                    ? material.is(TRAVELER_FORTUNA_TAG) && material.getItem() instanceof StellaFortuna
                    : material.is(target.getItem()))
                return i;
        }
        return -1;
    }

    private void predictUpgrade(int maxLevel) {
        this.mode = Mode.UPGRADE;

        int mora = this.getSlot(0).getItem().getOrCreateTag().getInt("moras");
        int lv = this.level;
        int exp = this.experience;
        int spent = 0;

        for (int i = 0; i < this.shrink.length && lv < maxLevel; i++) {
            ItemStack material = this.getSlot(2 + i).getItem();
            if (!(material.getItem() instanceof ExperienceBook book))
                continue;

            int value = book.getValue();
            if (value <= 0)
                continue;

            this.hasExpMaterial = true;
            // 与直接使用经验书保持一致：每点经验消耗 5 点摩拉。
            int cost = value * 5;
            while (this.shrink[i] < material.getCount() && lv < maxLevel) {
                if (spent + cost > mora)
                    break;

                spent += cost;
                this.shrink[i]++;
                exp += value;
                this.expGain += value;

                while (exp >= FortunaData.getExpReq(lv) && lv < maxLevel) {
                    exp -= FortunaData.getExpReq(lv);
                    lv++;
                }
            }
        }

        this.newLevel = lv;
        this.newExperience = exp;
        this.moraUse = spent;
        this.canEnhance = this.expGain > 0;
    }

    private void predictAscension(ItemStack stack) {
        if (this.entity instanceof ServerPlayer) {
            AscensionRecipe.Single recipe = getAscensionRecipe(stack, this.ascension);
            if (recipe == null) {
                this.ascensionPreview.set(0, ASCEND_STATE_MAX);
                this.mode = Mode.MAX;
                return;
            }

            this.ascensionPreview.set(1, this.ascension + 1);
            int mora = this.getSlot(0).getItem().getOrCreateTag().getInt("moras");
            boolean matched = this.matchIngredients(recipe);

            this.mode = Mode.ASCEND;
            this.newAscension = this.ascension + 1;
            if (matched && recipe.getMora() <= mora) {
                this.ascensionPreview.set(0, ASCEND_STATE_READY);
                this.moraUse = recipe.getMora();
                this.canEnhance = true;
            } else {
                this.ascensionPreview.set(0, ASCEND_STATE_MISSING);
            }
        } else {
            int state = this.ascensionPreview.get(0);
            switch (state) {
                case ASCEND_STATE_READY, ASCEND_STATE_MISSING -> {
                    this.mode = Mode.ASCEND;
                    this.newAscension = Math.max(this.ascension + 1, this.ascensionPreview.get(1));
                    this.canEnhance = state == ASCEND_STATE_READY;
                }
                case ASCEND_STATE_MAX -> this.mode = Mode.MAX;
                default -> {
                    /*
                     * 突破配方属于服务端数据，单人游戏里客户端也能读到；
                     * 专用服务器上要等服务端同步，同步前先按“不可点击”处理。
                     */
                    AscensionRecipe.Single recipe = getAscensionRecipe(stack, this.ascension);
                    if (recipe == null) {
                        this.mode = Mode.MAX;
                    } else {
                        this.mode = Mode.ASCEND;
                        this.newAscension = this.ascension + 1;
                    }
                }
            }
        }
    }

    private boolean matchIngredients(AscensionRecipe.Single recipe) {
        int used = 0;
        int[] counts = new int[this.shrink.length];

        for (int i = 0; i < 4; i++) {
            AscensionRecipe.Input input = recipe.getInput(i);
            if (input == null)
                continue;

            int slot = -1;
            for (int j = 0; j < this.shrink.length; j++) {
                if ((used & (1 << j)) != 0)
                    continue;
                if (input.match(this.getSlot(2 + j).getItem())) {
                    slot = j;
                    break;
                }
            }
            if (slot < 0)
                return false;

            used |= (1 << slot);
            counts[slot] = input.getCount();
        }

        System.arraycopy(counts, 0, this.shrink, 0, counts.length);
        return true;
    }

    private static AscensionRecipe.Single getAscensionRecipe(ItemStack stack, int ascension) {
        AscensionRecipe recipe = AscensionRecipeListener.getFortuna(stack.getItem());
        return recipe == null ? null : recipe.getRecipe(ascension);
    }

    /** 取技能升级配方：等级 1 升 2 使用第 0 条，依此类推。 */
    private static AscensionRecipe.Single getSkillRecipe(ItemStack stack, int level) {
        AscensionRecipe recipe = AscensionRecipeListener.getFortunaSkill(stack.getItem());
        return recipe == null ? null : recipe.getRecipe(level - 1);
    }

    /** 判断当前技能等级的配方是否满足（没有配方时视为无法升级）。 */
    private boolean canUseSkillRecipe(ItemStack stack, int level) {
        AscensionRecipe.Single recipe = getSkillRecipe(stack, level);
        if (recipe == null)
            return false;

        int mora = this.getSlot(0).getItem().getOrCreateTag().getInt("moras");
        return recipe.getMora() <= mora && this.hasIngredients(recipe);
    }

    /** 只检查材料是否齐全，不修改 shrink。 */
    private boolean hasIngredients(AscensionRecipe.Single recipe) {
        int used = 0;
        for (int i = 0; i < 4; i++) {
            AscensionRecipe.Input input = recipe.getInput(i);
            if (input == null)
                continue;

            int slot = -1;
            for (int j = 0; j < this.shrink.length; j++) {
                if ((used & (1 << j)) != 0)
                    continue;
                if (input.match(this.getSlot(2 + j).getItem())) {
                    slot = j;
                    break;
                }
            }
            if (slot < 0)
                return false;
            used |= 1 << slot;
        }
        return true;
    }

    /** 执行一次升级或突破。1 号槽位为空时，直接强化玩家当前装备的命星，并把结果同步回玩家数据。 */
    public void enhance() {
        this.predict();
        if (!this.canEnhance)
            return;

        // 1 号槽位为空时，强化玩家当前装备的命星。
        boolean equipped = this.getSlot(1).getItem().isEmpty();
        ItemStack stack = equipped
                ? this.getCapabilityStack()
                : this.getSlot(1).getItem();
        if (!(stack.getItem() instanceof StellaFortuna))
            return;

        boolean ascending = this.mode == Mode.ASCEND;
        int finalLevel = this.newLevel;
        int finalExperience = this.newExperience;
        int finalAscension = this.newAscension;

        for (int i = 0; i < this.shrink.length; i++) {
            if (this.shrink[i] > 0)
                this.getSlot(2 + i).getItem().shrink(this.shrink[i]);
        }
        if (this.moraUse > 0) {
            ItemStack bag = this.getSlot(0).getItem();
            CompoundTag tag = bag.getOrCreateTag();
            tag.putInt("moras", Math.max(0, tag.getInt("moras") - this.moraUse));
        }

        FortunaData current = FortunaData.get(stack);
        FortunaData updated = ascending
                ? current.withEnhance(current.level(), finalAscension, current.experience())
                : current.withEnhance(finalLevel, current.ascension(), finalExperience);
        this.applyFortunaData(stack, updated);
    }

    /**
     * 提升一级命座（消耗 2~6 号槽中的一个同种命星）。
     * 若目标是 empty_fortuna，则改为消耗一个带 er:traveler_fortuna 标签的牺牲命星，
     * 把空命星转化为该牺牲命星的物品类型，命座等级保持不变。
     */
    public void upgradeConstellation() {
        this.predict();
        if (!this.canUpgradeConstellation)
            return;

        ItemStack stack = this.getFortunaStack();
        if (!(stack.getItem() instanceof StellaFortuna))
            return;

        int materialSlot = this.findConstellationMaterial();
        if (materialSlot < 0)
            return;

        // 空命星：消耗一个旅行者命星，转化为该命星的类型，命座等级保持不变。
        if (stack.is(ErModItems.EMPTY_FORTUNA.get())) {
            ItemStack sacrifice = this.getSlot(2 + materialSlot).getItem();
            if (!sacrifice.is(TRAVELER_FORTUNA_TAG) || !(sacrifice.getItem() instanceof StellaFortuna))
                return;

            ItemStack transformed = new ItemStack(sacrifice.getItem());
            // 只保留原空命星的强化/命座/技能数据，其他数据使用新物品类型的默认值。
            FortunaData.get(stack).writeTo(transformed);
            sacrifice.shrink(1);
            this.replaceFortuna(transformed);
            return;
        }

        FortunaData current = FortunaData.get(stack);
        if (current.constellation() >= MAX_CONSTELLATION)
            return;

        this.getSlot(2 + materialSlot).getItem().shrink(1);

        FortunaData updated = new FortunaData(
                current.level(),
                current.ascension(),
                current.experience(),
                Math.min(MAX_CONSTELLATION, current.constellation() + 1),
                current.skills()
        );
        this.applyFortunaData(stack, updated);
    }

    /** 把目标命星替换成另一个物品类型（保留原物品上的数据）。 */
    private void replaceFortuna(ItemStack transformed) {
        boolean equipped = this.getSlot(1).getItem().isEmpty();
        if (equipped) {
            ErItemVariables.PlayerVariables vars = this.getVariables();
            if(vars.Stella_Fortuna.getItem() instanceof StellaFortuna fortuna)
                fortuna.removeAttr(this.entity);

            vars.Stella_Fortuna = transformed;
            vars.syncToClient(this.entity);
            if(transformed.getItem() instanceof StellaFortuna fortuna)
                fortuna.applyAttr(this.entity, FortunaData.get(transformed));
        } else {
            this.getSlot(1).set(transformed);
        }

        this.predict();
        this.broadcastChanges();
    }

    /**
     * 提升普攻/战技/爆发的等级。
     *
     * @param index 0 = 普攻，1 = 战技，2 = 爆发
     */
    public void upgradeSkill(int index) {
        this.predict();

        ItemStack stack = this.getFortunaStack();
        if (!(stack.getItem() instanceof StellaFortuna))
            return;

        FortunaData current = FortunaData.get(stack);
        int normal = current.skills().normal();
        int skill = current.skills().skill();
        int burst = current.skills().burst();

        int level;
        switch (index) {
            case 0 -> level = normal;
            case 1 -> level = skill;
            case 2 -> level = burst;
            default -> {
                return;
            }
        }
        if (level >= current.getMaxTalentLevel())
            return;

        // predict() 同时计算了强化页的经验/摩拉消耗，这里要先清空，避免误扣。
        Arrays.fill(this.shrink, 0);
        this.moraUse = 0;

        AscensionRecipe.Single recipe = getSkillRecipe(stack, level);
        if (recipe != null) {
            int mora = this.getSlot(0).getItem().getOrCreateTag().getInt("moras");
            if (recipe.getMora() > mora)
                return;
            if (!this.matchIngredients(recipe))
                return;
            this.moraUse = recipe.getMora();
        }

        switch (index) {
            case 0 -> normal++;
            case 1 -> skill++;
            case 2 -> burst++;
        }

        for (int i = 0; i < this.shrink.length; i++) {
            if (this.shrink[i] > 0)
                this.getSlot(2 + i).getItem().shrink(this.shrink[i]);
        }
        if (this.moraUse > 0) {
            ItemStack bag = this.getSlot(0).getItem();
            CompoundTag tag = bag.getOrCreateTag();
            tag.putInt("moras", Math.max(0, tag.getInt("moras") - this.moraUse));
        }

        FortunaData updated = new FortunaData(
                current.level(),
                current.ascension(),
                current.experience(),
                current.constellation(),
                new SkillData(normal, skill, burst)
        );
        this.applyFortunaData(stack, updated);
    }

    /** 把新的 FortunaData 写回槽位物品或玩家装备，并同步到客户端。 */
    private void applyFortunaData(ItemStack stack, FortunaData updated) {
        boolean equipped = this.getSlot(1).getItem().isEmpty();
        updated.writeTo(stack);

        if (equipped) {
            // 同步回玩家数据，并立即刷新命星提供的属性。
            ErItemVariables.PlayerVariables vars = this.getVariables();
            if(vars.Stella_Fortuna.getItem() instanceof StellaFortuna fortuna)
                fortuna.removeAttr(this.entity);

            vars.Stella_Fortuna = stack;
            vars.syncToClient(this.entity);
            if(stack.getItem() instanceof StellaFortuna fortuna)
                fortuna.applyAttr(this.entity, updated);
        } else {
            this.getSlot(1).setChanged();
        }

        this.predict();
        this.broadcastChanges();
    }

    private ItemStack getCapabilityStack() {
        return this.getVariables().Stella_Fortuna;
    }

    private ErItemVariables.PlayerVariables getVariables() {
        return this.entity.getCapability(ErItemVariables.PLAYER_VARIABLES)
                .orElseGet(ErItemVariables.PlayerVariables::new);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    public Mode getMode() {
        return this.mode;
    }

    public boolean canEnhance() {
        return this.canEnhance;
    }

    public boolean hasFortuna() {
        return this.getFortunaStack().getItem() instanceof StellaFortuna;
    }

    /** 目标是否为可被转化为旅行者命星的空命星。 */
    public boolean isEmptyFortuna() {
        return this.getFortunaStack().is(ErModItems.EMPTY_FORTUNA.get());
    }

    /**
     * 1 号槽位为空时，回退到玩家当前装备的命星。
     * 这样打开强化界面时不需要先把装备中的命星取下来再放进去。
     */
    public ItemStack getFortunaStack() {
        ItemStack stack = this.getSlot(1).getItem();
        if (!stack.isEmpty())
            return stack;
        return this.getCapabilityStack();
    }

    /** 供客户端每 tick 刷新预览，保证玩家数据同步后界面立即更新。 */
    public void refreshPreview() {
        this.predict();
    }

    public boolean hasExpMaterial() {
        return this.hasExpMaterial;
    }

    public int getLevel() {
        return this.level;
    }

    public int getExperience() {
        return this.experience;
    }

    public int getAscension() {
        return this.ascension;
    }

    public int getNewLevel() {
        return this.newLevel;
    }

    public int getNewExperience() {
        return this.newExperience;
    }

    public int getNewAscension() {
        return this.newAscension;
    }

    public int getExpGain() {
        return this.expGain;
    }

    public int getMoraUse() {
        return this.moraUse;
    }

    public int getConstellation() {
        return this.constellation;
    }

    public int getNormalLevel() {
        return this.normalLevel;
    }

    public int getSkillLevel() {
        return this.skillLevel;
    }

    public int getBurstLevel() {
        return this.burstLevel;
    }

    public boolean canUpgradeConstellation() {
        return this.canUpgradeConstellation;
    }

    public boolean canUpgradeNormal() {
        return this.canUpgradeNormal;
    }

    public boolean canUpgradeSkill() {
        return this.canUpgradeSkill;
    }

    public boolean canUpgradeBurst() {
        return this.canUpgradeBurst;
    }
}