package net.wither.er.artifact_effect;

import com.mojang.serialization.Codec;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.init.AdditionalRegistries;

public class ArtifactEffect {
    public static final Codec<Holder<ArtifactEffect>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ArtifactEffect>> STREAM_CODEC;
    private static final ResourceLocation BERSERKER_LOW = ResourceLocation.parse("er:berserker_low_hp");

    static {
        CODEC = AdditionalRegistries.ARTIFACT_REGISTRY.holderByNameCodec();
        STREAM_CODEC = ByteBufCodecs.holderRegistry(AdditionalRegistries.ARTIFACT_EFFECT);
    }

    public static void berserkerCheck(Entity entity){
        if(entity instanceof LivingEntity livingEntity) {
            AttributeInstance instance = livingEntity.getAttribute(ErModAttributes.CRIT_RATE);
            if(instance == null) return;

            instance.removeModifier(BERSERKER_LOW);
            if (((ErEntityInterface)entity).er$getArtifactEffectLevel(ArtifactEffectRegistry.BERSERKER) > 3) {
                if (livingEntity.getHealth() <= livingEntity.getMaxHealth() * 0.7)
                    instance.addPermanentModifier(new AttributeModifier(BERSERKER_LOW, 0.24, AttributeModifier.Operation.ADD_VALUE));
            }
        }
    }
}
