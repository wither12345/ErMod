package net.wither.er.artifact_effect;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.init.AdditionalRegistries;

public class ArtifactEffectRegistry {
    public static final DeferredRegister<ArtifactEffect> ARTIFACT_EFFECTS = DeferredRegister.create(AdditionalRegistries.ARTIFACT_REGISTRY, ErMod.MODID);
    public static final DeferredHolder<ArtifactEffect,ArtifactEffect> EMPTY = ARTIFACT_EFFECTS.register("empty",
            ArtifactEffect::new
    );
    public static final DeferredHolder<ArtifactEffect,ArtifactEffect> ADVENTURER = ARTIFACT_EFFECTS.register("adventurer",
            () -> new TwoSetAttrEffect(Attributes.MAX_HEALTH, new AttributeModifier(ResourceLocation.parse("er:adventure"), 20, AttributeModifier.Operation.ADD_VALUE))
    );
    public static final DeferredHolder<ArtifactEffect,ArtifactEffect> LUCKY_DOG = ARTIFACT_EFFECTS.register("lucky_dog",
            () -> new TwoSetAttrEffect(Attributes.ARMOR, new AttributeModifier(ResourceLocation.parse("er:lucky_dog"), 100, AttributeModifier.Operation.ADD_VALUE))
    );
    public static final DeferredHolder<ArtifactEffect,ArtifactEffect> TRAVELING_DOCTOR = ARTIFACT_EFFECTS.register("traveling_doctor",
            () -> new TwoSetAttrEffect(ErModAttributes.INCOMING_HEALING_BONUS, new AttributeModifier(ResourceLocation.parse("er:traveling_doctor"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE))
    );
    public static final DeferredHolder<ArtifactEffect,ArtifactEffect> BERSERKER = ARTIFACT_EFFECTS.register("berserker",
            () -> new TwoSetAttrEffect(ErModAttributes.CRIT_RATE, new AttributeModifier(ResourceLocation.parse("er:berserker"), 0.12, AttributeModifier.Operation.ADD_VALUE))
    );
}
