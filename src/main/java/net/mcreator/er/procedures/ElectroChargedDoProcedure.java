package net.mcreator.er.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import net.mcreator.er.init.ErModAttributes;

public class ElectroChargedDoProcedure {
	public static void execute(LevelAccessor world, Entity entity, double amplifier) {
		if (entity == null)
			return;
		entity.hurt(new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:reaction")))),
				(float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * (1 + 16 * (amplifier / (amplifier + 20))) * 0.1
						* (1 - (entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(ErModAttributes.ELECTRO_RES) ? _livingEntity1.getAttribute(ErModAttributes.ELECTRO_RES).getValue() : 0) / 100)));
	}
}