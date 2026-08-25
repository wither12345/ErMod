package net.wither.er.item.artifact_effect;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.elements.AuraContainer;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.init.MobEffectRegister;
import net.wither.er.item.data.weapon.ReactionAbility;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Instructor extends TwoSetAttrEffect implements ReactionAbility {
    private static final AttributeModifier MODIFIER = new AttributeModifier(UUID.fromString("6A77C1CD-1C77-1A97-931F-2C03E892466E"), "er:instructor", 80, AttributeModifier.Operation.ADDITION);

    public Instructor() {
        super(ErModAttributes.ELEMENTAL_MASTERY.get(), MODIFIER);
    }

    @Override
    public void onReaction(AuraContainer container, ElementSource elementToAdd, Element elementReacted, EntityHurtEvent.DamageModifier damageModifier, @NotNull Entity applier, int level) {
        if(level > 3)
            applier.level().getEntitiesOfClass(LivingEntity.class, applier.getBoundingBox().inflate(8), entity -> !EntityHurtEvent.shouldHurt(entity, applier))
                .forEach(living -> living.addEffect(new MobEffectInstance(MobEffectRegister.INSTRUCTOR_BLESS.get(), 160)));
    }
}
