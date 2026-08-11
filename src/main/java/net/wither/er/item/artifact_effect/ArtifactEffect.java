package net.wither.er.item.artifact_effect;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.wither.er.init.AdditionalRegistries;

public class ArtifactEffect {
    public static final Codec<Holder<ArtifactEffect>> CODEC  = RegistryFixedCodec.create(AdditionalRegistries.ARTIFACT_EFFECT);
}
