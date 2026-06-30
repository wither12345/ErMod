package net.wither.er.shield;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public abstract class ErShield {

	abstract ResourceLocation getKbResLocation() ;

	public void start(Entity owner) {
		if(owner instanceof  LivingEntity living)
			living.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addOrReplacePermanentModifier(new AttributeModifier(getKbResLocation(), 100, AttributeModifier.Operation.ADD_VALUE));
	}

	public boolean tick(ShieldStack stack, Entity owner) {
		return stack.time-- > 0;
	}

	public void end(Entity owner) {
		if(owner instanceof LivingEntity living)
			living.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(new AttributeModifier(getKbResLocation(), 100, AttributeModifier.Operation.ADD_VALUE));
	}


	public abstract float onHurt(ShieldStack stack, Entity owner, DamageSource source, float damage, int elemental_type);
}
