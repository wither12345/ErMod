package net.mcreator.er.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.er.entity.TravelerTornadoEntity;

public class TravelerTornadoAnemoProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		return (entity instanceof TravelerTornadoEntity _datEntI ? _datEntI.getEntityData().get(TravelerTornadoEntity.DATA_Absorption) : 0) == 0;
	}
}