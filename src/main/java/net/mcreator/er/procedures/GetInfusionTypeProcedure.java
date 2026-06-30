package net.mcreator.er.procedures;

import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.er.MultipleInfusion;

public class GetInfusionTypeProcedure {
	public static double execute(Entity entity, Entity immediatesourceentity) {
		if (entity == null)
			return 0;
		if (immediatesourceentity == (Entity) entity) {
			if (((LivingEntity) entity).getMainHandItem().getItem() instanceof MultipleInfusion) {
				Item item = ((LivingEntity) entity).getMainHandItem().getItem();
				return ((MultipleInfusion) item).getInfusion(((LivingEntity) entity).getMainHandItem(), entity);
			}
			if (IsAnemoInfusionProcedure.execute(null,entity)) {
				return 1;
			} else if (IsCryoInfusionProcedure.execute(null,entity)) {
				return 2;
			} else if (IsDendroInfusionProcedure.execute(null,entity)) {
				return 3;
			} else if (IsElectroInfusionProcedure.execute(null,entity)) {
				return 4;
			} else if (IsGeoInfusionProcedure.execute(null,entity)) {
				return 5;
			} else if (IsHydroInfusionProcedure.execute(null,entity)) {
				return 6;
			} else if (IsPyroInfusionProcedure.execute(null,entity)) {
				return 7;
			}
		} else {
			if (immediatesourceentity instanceof LargeFireball || immediatesourceentity instanceof SmallFireball) {
				return 7;
			} else {
				return immediatesourceentity.getPersistentData().getDouble("Element");
			}
		}
		return 0;

	}
}
