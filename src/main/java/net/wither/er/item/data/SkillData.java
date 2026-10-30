package net.wither.er.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SkillData(int normal, int skill, int burst) {
    public static final SkillData DEFAULT = new SkillData(1, 1, 1);

    public static final Codec<SkillData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("normal").forGetter(SkillData::normal),
                    Codec.INT.fieldOf("skill").forGetter(SkillData::skill),
                    Codec.INT.fieldOf("burst").forGetter(SkillData::burst)
            ).apply(instance, SkillData::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SkillData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SkillData::normal,
            ByteBufCodecs.INT, SkillData::skill,
            ByteBufCodecs.INT, SkillData::burst,
            SkillData::new
    );
    
}
