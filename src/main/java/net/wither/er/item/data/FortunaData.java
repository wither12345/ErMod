package net.wither.er.item.data;

import net.minecraft.world.entity.Entity;
import net.wither.er.item.fortuna.StellaFortuna;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.wither.er.network.ErItemVariables;

public record FortunaData(int level, int ascension, int experience, int constellation, SkillData skills) {
    /** 从物品 NBT 读取命星（Fortuna）数据，缺失时返回默认值。物品中保存的 level 从 0 开始，这里转为从 1 开始的展示等级。 */
    public static FortunaData get(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !(stack.getItem() instanceof StellaFortuna))
            return new FortunaData(1, 0, 0, 0, SkillData.DEFAULT);
        int level = tag.contains("level") ? tag.getInt("level") + 1 : 1;
        return new FortunaData(
                level,
                tag.getInt("ascension"),
                tag.getInt("experience"),
                tag.getInt("constellation"),
                new SkillData(
                        tag.contains("normal") ? tag.getInt("normal") : 1,
                        tag.contains("skill") ? tag.getInt("skill") : 1,
                        tag.contains("burst") ? tag.getInt("burst") : 1
                )
        );
    }

    /** 从实体上读取 FortunaData */
    public static FortunaData get(Entity entity){
        ItemStack stack = entity.getCapability(ErItemVariables.PLAYER_VARIABLES).map(var -> var.Stella_Fortuna).orElse(ItemStack.EMPTY);
        return get(stack);
    }

    /** 仅更新强化相关的 level/ascension/experience，保留 normal/skill/burst 等级。 */
    public FortunaData withEnhance(int newLevel, int newAscension, int newExperience) {
        return new FortunaData(newLevel, newAscension, newExperience, this.constellation, this.skills);
    }

    /** 获取普通攻击等级
     * @return 普通攻击等级
     * */
    public int normal() {
        return this.skills().normal();
    }

    /** 获取普通攻击等级，命之座足够bonus时额外+3
     * @param bonus 命之座要求等级
     * @return 普通攻击等级
     *  */
    public int normal(int bonus){
        return this.skills().normal() + this.constellation >= bonus ? 3 : 0;
    }

    /** 获取战技等级
     * @return 战技等级
     * */
    public int skill() {
        return this.skills().skill();
    }

    /** 获取战技等级，命之座足够bonus时额外+3
     * @param bonus 命之座要求等级
     * @return 战技等级
     *  */
    public int skill(int bonus){
        return this.skills().skill() + this.constellation >= bonus ? 3 : 0;
    }

    /** 获取爆发等级
     * @return 爆发等级
     * */
    public int burst() {
        return this.skills().burst();
    }

    /** 获取爆发等级，命之座足够bonus时额外+3
     * @param bonus 命之座要求等级
     * @return 爆发等级
     *  */
    public int burst(int bonus){
        return this.skills().burst() + this.constellation >= bonus ? 3 : 0;
    }

    /** 获取天赋最大等级
     * @return 最大等级
     */
    public int getMaxTalentLevel(){
        return switch (this.ascension) {
            case 0 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 6;
            case 5 -> 8;
            case 6 -> 10;
            default -> this.ascension * 2;
        };
    }

    /** 把数据写回物品 NBT。与 StellaFortuna 保持一致，level 以 0 为基准存储。 */
    public void writeTo(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("level", Math.max(0, this.level - 1));
        tag.putInt("ascension", this.ascension);
        tag.putInt("experience", this.experience);
        tag.putInt("constellation", this.constellation);
        tag.putInt("normal", this.skills().normal());
        tag.putInt("skill", this.skills().skill());
        tag.putInt("burst", this.skills().burst());
    }

    /** 获取升级需要的经验值
     * @param level 目标等级
     * @return 升级需要的经验值
     */
    public static int getExpReq(int level) {
        if (level <= 20)
            return (int) (0.402 * level * level + 12.31 * level + 27.49);
        if (level <= 40)
            return (int) (0.555 * level * level + 17.56 * level - 46.63);
        if (level <= 60)
            return (int) (1.014 * level * level + 29.00 * level - 421.2);
        if (level <= 80)
            return (int) (1.815 * level * level + 43.60 * level - 1012);
        return (int) (2300 * Math.exp(0.165 * (level - 80)));
    }


    /** 获取最大等级
     * @param ascension 强化等级
     * @return 最大等级
     * */
    public static int getMaxLevel(int ascension) {
        return switch (ascension) {
            case 0 -> 20;
            case 1 -> 40;
            case 2 -> 50;
            case 3 -> 60;
            case 4 -> 70;
            case 5 -> 80;
            default -> 90;
        };
    }
}