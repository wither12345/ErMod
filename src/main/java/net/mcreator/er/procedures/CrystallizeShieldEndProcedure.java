package net.mcreator.er.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;

public class CrystallizeShieldEndProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("er.shield.knockback_res");
		((LivingEntity) entity).getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(new AttributeModifier(resourcelocation, 100, AttributeModifier.Operation.ADD_VALUE));
		((LivingEntity) entity).setAbsorptionAmount(Math.max(((LivingEntity) entity).getAbsorptionAmount() - 10, 0));
	}
}
