package net.mcreator.er.procedures;

import net.minecraft.world.entity.Entity;

public class ElementalProjectileUpdateProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.setNoGravity(true);
	}
}