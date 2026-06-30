package net.mcreator.er.procedures;

import net.wither.er.elements.ElementSource;
import net.wither.er.init.ElementRegistry;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.er.entity.MistFlowerEntity;

import java.util.Comparator;

public class MistFlowerTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (world instanceof Level level && level.getGameTime() % 20 == 0 && entity.isAlive()) {
			{
				final Vec3 _center = new Vec3(x, y, z);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (!(entityiterator instanceof MistFlowerEntity) && entityiterator instanceof LivingEntity) {
						entityiterator.hurt(ElementSource.createDamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), entity,
								new ElementSource(ElementRegistry.CRYO.get(), new ResourceLocation("er:mob_attack"), 1, true)), 3);
					}
				}
			}
		}
	}
}