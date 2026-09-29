package net.wither.er.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DelusionData(float critMulti, float dmgMulti, float healthConsume) {
    public static final Codec<DelusionData> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, DelusionData> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.FLOAT.fieldOf("level").forGetter(DelusionData::critMulti),
                        Codec.FLOAT.fieldOf("experience").forGetter(DelusionData::dmgMulti),
                        Codec.FLOAT.fieldOf("total_experience").forGetter(DelusionData::healthConsume)
                ).apply(instance, DelusionData::new));
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, DelusionData::critMulti,
                ByteBufCodecs.FLOAT, DelusionData::dmgMulti,
                ByteBufCodecs.FLOAT, DelusionData::healthConsume,
                DelusionData::new
        ) ;
    }
}
