package net.wither.er.shield;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public abstract class ErShield {
	abstract UUID getKbResLocation() ;

	public void start(Entity owner) {
		if(owner instanceof  LivingEntity living) {
			living.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(getKbResLocation());
			living.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier(getKbResLocation(), "shield", 100, AttributeModifier.Operation.ADDITION));
		}
	}

	public boolean tick(ShieldStack stack, Entity owner) {
		return stack.time-- > 0;
	}

	public void end(Entity owner) {
		if(owner instanceof LivingEntity living)
			living.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(getKbResLocation());
	}


	public abstract float onHurt(ShieldStack stack, Entity owner, DamageSource source, float damage, int elemental_type);
}
