package net.wither.er.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.wither.er.item.fortuna.StellaFortuna;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.network.ErItemVariables;

public record FortunaData(int level, int ascension, int experience, int constellation, SkillData skills) {
    public static final Codec<FortunaData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("level").forGetter(FortunaData::level),
                    Codec.INT.fieldOf("ascension").forGetter(FortunaData::ascension),
                    Codec.INT.fieldOf("experience").forGetter(FortunaData::experience),
                    Codec.INT.optionalFieldOf("constellation", 0).forGetter(FortunaData::constellation),
                    SkillData.CODEC.lenientOptionalFieldOf("skills", SkillData.DEFAULT).forGetter(FortunaData::skills)
            ).apply(instance, FortunaData::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, FortunaData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FortunaData::level,
            ByteBufCodecs.INT, FortunaData::ascension,
            ByteBufCodecs.INT, FortunaData::experience,
            ByteBufCodecs.INT, FortunaData::constellation,
            SkillData.STREAM_CODEC, FortunaData::skills,
            FortunaData::new
    );

    /** 从物品上读取 FortunaData，并把旧版 CustomData 数据迁移到新组件。 */
    public static FortunaData get(ItemStack stack) {
        FortunaData component = stack.get(DataComponentsRegister.FORTUNA.get());
        if (component != null) {
            // 新组件已经存在时，顺手清掉旧字段，避免两套数据同时存在。
            if (stack.getItem() instanceof StellaFortuna)
                clearLegacyData(stack);
            return component;
        }

        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null && stack.getItem() instanceof StellaFortuna) {
            CompoundTag tag = data.copyTag();
            if (tag.contains("level")) {
                // 旧版 level 从 0 开始，新组件统一使用从 1 开始的等级。
                FortunaData migrated = new FortunaData(
                        tag.getInt("level") + 1,
                        tag.getInt("ascension"),
                        tag.getInt("experience"),
                        tag.getInt("constellation"),
                        SkillData.DEFAULT
                );
                stack.set(DataComponentsRegister.FORTUNA.get(), migrated);
                clearLegacyData(stack);
                return migrated;
            }
        }

        return new FortunaData(1, 0, 0, 0, SkillData.DEFAULT);
    }

    /** 从实体上读取 FortunaData */
    public static FortunaData get(Entity entity){
        ItemStack stack = entity.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna;
        return get(stack);
    }

    /** 删除旧版写在 CustomData 里的强化字段，其他自定义数据保持不变。 */
    private static void clearLegacyData(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null)
            return;

        CompoundTag tag = data.copyTag();
        if (!tag.contains("level") && !tag.contains("ascension") && !tag.contains("experience"))
            return;

        tag.remove("level");
        tag.remove("ascension");
        tag.remove("experience");
        if (tag.isEmpty())
            stack.remove(DataComponents.CUSTOM_DATA);
        else
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    /** 只更新强化相关的 level/ascension/experience，保留 normal/skill/burst 等级。 */
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

    /** 获取升级需要的经验值
     * @param level 等级
     * @return 经验值
     * */
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
        return switch (ascension){
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