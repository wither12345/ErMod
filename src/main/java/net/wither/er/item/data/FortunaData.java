package net.wither.er.item.data;

import net.wither.er.item.fortuna.StellaFortuna;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record FortunaData(int level, int ascension, int experience, int normal_level, int skill_level, int burst_level) {
    /** 从物品 NBT 读取命星（Fortuna）数据，缺失时返回默认值。物品中保存的 level 从 0 开始，这里转为从 1 开始的展示等级。 */
    public static FortunaData get(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !(stack.getItem() instanceof StellaFortuna))
            return new FortunaData(1, 0, 0, 0, 0, 0);
        int level = tag.contains("level") ? tag.getInt("level") + 1 : 1;
        return new FortunaData(
                level,
                tag.getInt("ascension"),
                tag.getInt("experience"),
                tag.getInt("normal_level"),
                tag.getInt("skill_level"),
                tag.getInt("burst_level"));
    }

    /** 仅更新强化相关的 level/ascension/experience，保留 normal/skill/burst 等级。 */
    public FortunaData withEnhance(int newLevel, int newAscension, int newExperience) {
        return new FortunaData(newLevel, newAscension, newExperience, this.normal_level, this.skill_level, this.burst_level);
    }

    /** 把数据写回物品 NBT。与 StellaFortunas 保持一致，level 以 0 为基准存储。 */
    public void writeTo(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("level", Math.max(0, this.level - 1));
        tag.putInt("ascension", this.ascension);
        tag.putInt("experience", this.experience);
        tag.putInt("normal_level", this.normal_level);
        tag.putInt("skill_level", this.skill_level);
        tag.putInt("burst_level", this.burst_level);
    }

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

    public static int getMaxLevel(int ascension) {
        return switch (ascension) {
            case 0 -> 20;
            case 1 -> 40;
            case 2 -> 50;
            case 3 -> 60;
            case 4 -> 70;
            case 8 -> 80;
            default -> 90;
        };
    }
}