package net.mcreator.er.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.er.EntityHurtEvent;

import java.util.Comparator;

public class VacuumFieldsDisplacingProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, double range, double strength) {
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(range / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entity == null || EntityHurtEvent.shouldHurt(entity, entityiterator)) {
					entityiterator.push(Math.min(1, Math.max((x - entityiterator.getX()) * strength
							* (1 - (entityiterator instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE) ? _livingEntity1.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue() : 0)),
							-1)),
							Math.min(1,
									Math.max((y - entityiterator.getY()) * strength
											* (1 - (entityiterator instanceof LivingEntity _livingEntity3 && _livingEntity3.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)
													? _livingEntity3.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue()
													: 0)),
											-1)),
							Math.min(1,
									Math.max((z - entityiterator.getZ()) * strength
											* (1 - (entityiterator instanceof LivingEntity _livingEntity5 && _livingEntity5.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)
													? _livingEntity5.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue()
													: 0)),
											-1)));
				}
			}
		}
	}
}