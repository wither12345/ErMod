package net.wither.er.artifact_effect;

import com.mojang.serialization.Codec;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.init.AdditionalRegistries;

import java.util.UUID;

public class ArtifactEffect {
    public static final Codec<Holder<ArtifactEffect>> CODEC;
    private static final String BERSERKER_LOW = "er:berserker_low_hp";
    private static final UUID BERSERKER = UUID.fromString("5F92757D-9E1B-8DA1-B0D0-E656B4F5416D");

    static {
        CODEC = RegistryFixedCodec.create(AdditionalRegistries.ARTIFACT_EFFECT);
    }

    public static void BerserkerCheck(Entity entity){
        if(entity instanceof LivingEntity livingEntity) {
            AttributeInstance instance = livingEntity.getAttribute(ErModAttributes.CRIT_RATE.get());
            if(instance == null) return;

            instance.removeModifier(BERSERKER);
            if (((ErEntityInterface)entity).er$getArtifactEffectLevel(ArtifactEffectRegistry.BERSERKER.get()) > 3) {
                if (livingEntity.getHealth() <= livingEntity.getMaxHealth() * 0.7)
                    instance.addTransientModifier(new AttributeModifier(BERSERKER, BERSERKER_LOW, 0.24, AttributeModifier.Operation.ADDITION));
            }
        }
    }
}
