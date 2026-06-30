package net.mcreator.er.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.particles.SimpleParticleType;

import net.mcreator.er.init.ErModParticleTypes;
import net.mcreator.er.entity.BlossomOfWealthEntity;

public class BlossomOfWealthTickingProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (world.isClientSide()) {
			if ((entity instanceof BlossomOfWealthEntity _datEntI ? _datEntI.getEntityData().get(BlossomOfWealthEntity.DATA_OmenLevel) : 0) > Math.random() * 5) {
				for (int index0 = 0; index0 < 3; index0++) {
					world.addParticle((SimpleParticleType) (ErModParticleTypes.BLOSSOM_OMEN_PARTICLE.get()), entity.getRandomX(5.0), entity.getRandomY(), entity.getRandomZ(5.0), 0, 0, 0);
				}
			} else {
				for (int index1 = 0; index1 < 3; index1++) {
					if (entity instanceof BlossomOfWealthEntity) {
						world.addParticle((SimpleParticleType) (ErModParticleTypes.BLOSSOM_OF_WEALTH_PARTICLE.get()), entity.getRandomX(5.0), entity.getRandomY(), entity.getRandomZ(5.0), 0, 0, 0);
					} else {
						world.addParticle((SimpleParticleType) (ErModParticleTypes.BLOSSOM_OF_REVELATION_PARTICLE.get()), entity.getRandomX(5.0), entity.getRandomY(), entity.getRandomZ(5.0), 0, 0, 0);
					}
				}
			}
		}
	}
}