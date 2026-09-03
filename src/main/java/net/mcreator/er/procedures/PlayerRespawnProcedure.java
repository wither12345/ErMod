package net.mcreator.er.procedures;

import net.wither.er.network.ErItemVariables;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.StellaFortunas;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PlayerRespawnProcedure {
	@SubscribeEvent
	public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		ErItemVariables.PlayerVariables _vars = entity.getData(ErItemVariables.PLAYER_VARIABLES);
		StellaFortunas.applyAttr((LivingEntity) entity, _vars.Stella_Fortuna);
		_vars.syncPlayerVariables(entity);
		if (entity instanceof LivingEntity _livingEntity0 && _livingEntity0.getAttributes().hasAttribute(ErModAttributes.CRIT_DAMAGE))
			_livingEntity0.getAttribute(ErModAttributes.CRIT_DAMAGE).setBaseValue(0.5);
		if (entity instanceof LivingEntity _entity)
			_entity.setHealth(entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
	}
}