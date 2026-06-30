package net.wither.er.artifact_effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.wither.er.init.AdditionalRegistries;

import java.util.UUID;
import java.util.function.Supplier;

public class ArtifactEffectRegistry {
    public static final Supplier<ArtifactEffect> EMPTY = AdditionalRegistries.ARTIFACT_EFFECTS.register("empty",
            ArtifactEffect::new
    );
    public static final Supplier<ArtifactEffect> ADVENTURER = AdditionalRegistries.ARTIFACT_EFFECTS.register("adventurer",
            () -> new TwoSetAttrEffect(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("B4F18551-C180-B888-ACCA-A4962ACDB56D"), "adventurer", 20, AttributeModifier.Operation.ADDITION))
    );
    public static final Supplier<ArtifactEffect> LUCKY_DOG = AdditionalRegistries.ARTIFACT_EFFECTS.register("lucky_dog",
            () -> new TwoSetAttrEffect(Attributes.ARMOR, new AttributeModifier(UUID.fromString("2635FFBD-AC7E-9CD9-7B9F-CBB530A6FA9A"), "lucky_dog", 100, AttributeModifier.Operation.ADDITION))
    );
    public static final Supplier<ArtifactEffect> TRAVELING_DOCTOR = AdditionalRegistries.ARTIFACT_EFFECTS.register("traveling_doctor",
            () -> new TwoSetAttrEffect(ErModAttributes.INCOMING_HEALING_BONUS.get(), new AttributeModifier(UUID.fromString("CA95A88C-A643-4928-22FB-CBC73183C84A"), "traveling_doctor" , 0.2, AttributeModifier.Operation.MULTIPLY_BASE))
    );
    public static final Supplier<ArtifactEffect> BERSERKER = AdditionalRegistries.ARTIFACT_EFFECTS.register("berserker",
            () -> new TwoSetAttrEffect(ErModAttributes.CRIT_RATE.get(), new AttributeModifier(UUID.fromString("C90A991A-97E2-B100-76C5-AF332D025984"), "berserker" , 0.12, AttributeModifier.Operation.ADDITION))
    );
}
