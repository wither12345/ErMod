package net.wither.er.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.wither.er.item.StellaFortuna;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.wither.er.init.DataComponentsRegister;

public record FortunaData(int level, int ascension, int experience, int normal_level, int skill_level, int burst_level) {
    public static final Codec<FortunaData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("level").forGetter(FortunaData::level),
                    Codec.INT.fieldOf("ascension").forGetter(FortunaData::ascension),
                    Codec.INT.fieldOf("experience").forGetter(FortunaData::experience),
                    Codec.INT.fieldOf("normal_level").forGetter(FortunaData::normal_level),
                    Codec.INT.fieldOf("skill_level").forGetter(FortunaData::skill_level),
                    Codec.INT.fieldOf("burst_level").forGetter(FortunaData::burst_level)
            ).apply(instance, FortunaData::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, FortunaData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FortunaData::level,
            ByteBufCodecs.INT, FortunaData::ascension,
            ByteBufCodecs.INT, FortunaData::experience,
            ByteBufCodecs.INT, FortunaData::normal_level,
            ByteBufCodecs.INT, FortunaData::skill_level,
            ByteBufCodecs.INT, FortunaData::burst_level,
            FortunaData::new
    );

    /** 从物品上读取 FortunaData，缺失时返回全 0 的默认值。 */
    public static FortunaData get(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if(data != null && data.copyTag().contains("level") && stack.getItem() instanceof StellaFortuna){
            FortunaData ret = new FortunaData(
                    data.copyTag().getInt("level") + 1,
                    data.copyTag().getInt("ascension"),
                    data.copyTag().getInt("experience"),
                    0,0,0
            );
            data.copyTag().remove("level");
            data.copyTag().remove("ascension");
            data.copyTag().remove("experience");
            return ret;
        }
        return stack.getOrDefault(DataComponentsRegister.FORTUNA.get(), new FortunaData(1, 0, 0, 0, 0, 0));
    }

    /** 只更新强化相关的 level/ascension/experience，保留 normal/skill/burst 等级。 */
    public FortunaData withEnhance(int newLevel, int newAscension, int newExperience) {
        return new FortunaData(newLevel, newAscension, newExperience, this.normal_level, this.skill_level, this.burst_level);
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
        return switch (ascension){
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