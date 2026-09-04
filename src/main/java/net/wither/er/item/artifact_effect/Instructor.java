package net.wither.er.item.artifact_effect;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.elements.AuraContainer;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.init.EffectRegister;
import net.wither.er.item.data.weapon.ReactionAbility;
import org.jetbrains.annotations.NotNull;

public class Instructor extends AttrArtifactEffect implements ReactionAbility {
    private static final AttributeModifier MODIFIER = new AttributeModifier(ResourceLocation.parse("er:instructor"), 80, AttributeModifier.Operation.ADD_VALUE);

    public Instructor() {
        super(ErModAttributes.ELEMENTAL_MASTERY, MODIFIER);
    }

    @Override
    public void onReaction(AuraContainer container, ElementSource elementToAdd, Element.Category elementReacted, EntityHurtEvent.DamageModifier damageModifier, @NotNull Entity applier, int level) {
        if(level > 3)
            applier.level().getEntitiesOfClass(LivingEntity.class, applier.getBoundingBox().inflate(8), entity -> !EntityHurtEvent.shouldHurt(entity, applier))
                    .forEach(living -> living.addEffect(new MobEffectInstance(EffectRegister.INSTRUCTOR_BLESS, 160)));
    }
}
