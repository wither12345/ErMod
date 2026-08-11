package net.wither.er.item.artifact_effect;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.wither.er.init.AdditionalRegistries;

public class ArtifactEffect {
    public static final Codec<Holder<ArtifactEffect>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ArtifactEffect>> STREAM_CODEC;

    static {
        CODEC = AdditionalRegistries.ARTIFACT_REGISTRY.holderByNameCodec();
        STREAM_CODEC = ByteBufCodecs.holderRegistry(AdditionalRegistries.ARTIFACT_EFFECT);
    }
}
