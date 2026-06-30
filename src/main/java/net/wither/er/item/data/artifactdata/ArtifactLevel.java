package net.wither.er.item.data.artifactdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ArtifactLevel(int level, int experience, int total_experience) {
    public static final Codec<ArtifactLevel> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, ArtifactLevel> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("level").forGetter(ArtifactLevel::level),
                        Codec.INT.fieldOf("experience").forGetter(ArtifactLevel::experience),
                        Codec.INT.fieldOf("total_experience").forGetter(ArtifactLevel::total_experience)
                ).apply(instance, ArtifactLevel::new));
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, ArtifactLevel::level,
                ByteBufCodecs.INT, ArtifactLevel::experience,
                ByteBufCodecs.INT, ArtifactLevel::total_experience,
                ArtifactLevel::new
        ) ;
    }
}
