package net.mcreator.er.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.er.init.ErModMobEffects;
import net.mcreator.er.entity.BlossomOfWealthEntity;

public class BlossomOfWealthOnClickProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (sourceentity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(ErModMobEffects.DISORDER_OMEN.get())) {
			if (sourceentity instanceof LivingEntity _livEnt1 && _livEnt1.hasEffect(MobEffects.BAD_OMEN)) {
				if (sourceentity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(ErModMobEffects.DISORDER_OMEN.get(),
							18000 * (sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.BAD_OMEN) ? _livEnt.getEffect(MobEffects.BAD_OMEN).getAmplifier() : 0) + 18000,
							sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.BAD_OMEN) ? _livEnt.getEffect(MobEffects.BAD_OMEN).getAmplifier() : 0));
			}
			if (entity instanceof BlossomOfWealthEntity _datEntSetI)
				_datEntSetI.getEntityData().set(BlossomOfWealthEntity.DATA_OmenLevel,
						(sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ErModMobEffects.DISORDER_OMEN.get()) ? _livEnt.getEffect(ErModMobEffects.DISORDER_OMEN.get()).getAmplifier() : 0) + 1);
		} else if (sourceentity instanceof LivingEntity _livEnt7 && _livEnt7.hasEffect(MobEffects.BAD_OMEN)) {
			if (sourceentity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(
						new MobEffectInstance(ErModMobEffects.DISORDER_OMEN.get(), 18000 * (sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.BAD_OMEN) ? _livEnt.getEffect(MobEffects.BAD_OMEN).getAmplifier() : 0) + 18000,
								sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.BAD_OMEN) ? _livEnt.getEffect(MobEffects.BAD_OMEN).getAmplifier() : 0));
			if (entity instanceof BlossomOfWealthEntity _datEntSetI)
				_datEntSetI.getEntityData().set(BlossomOfWealthEntity.DATA_OmenLevel, (sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.BAD_OMEN) ? _livEnt.getEffect(MobEffects.BAD_OMEN).getAmplifier() : 0) + 1);
			if (sourceentity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.BAD_OMEN);
		}
	}
}