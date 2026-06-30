package net.mcreator.er.procedures;

import net.wither.er.shield.ShieldStack;
import net.wither.er.shield.ShieldRegistry;
import net.wither.er.entity.ErEntityInterface;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import java.util.Comparator;

public class HydroCrystallizeTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) >= 0.05) {
			if (entity instanceof LivingEntity _entity)
				_entity.setHealth((float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) - 0.05));
			{
				final Vec3 _center = new Vec3(x, y, z);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (entityiterator instanceof ErEntityInterface enti && (entityiterator.getStringUUID()).equals(entity.getPersistentData().getString("UUID"))) {
						enti.addShield(new ShieldStack(ShieldRegistry.HYDRO_CTYSTALLIZE.get(), entity.getPersistentData().getFloat("health"), 200));
						if (!entity.level().isClientSide())
							entity.discard();
					}
				}
			}
		} else {
			if (!entity.level().isClientSide())
				entity.discard();
		}
	}
}