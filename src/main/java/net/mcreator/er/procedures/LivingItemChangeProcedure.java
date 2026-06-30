package net.mcreator.er.procedures;


import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class LivingItemChangeProcedure {
	@SubscribeEvent
	public static void onPickup(LivingSwapItemsEvent.Hands event) {
		//execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
			/*
		if (entity instanceof Skeleton && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ErModItems.HUNTERS_BOW.get()) {
			GoalSelector goalSelector = ((Mob) entity).goalSelector;
			goalSelector.removeGoal(new MeleeAttackGoal((PathfinderMob) entity, 1.2D, false));
			goalSelector.addGoal(4, new RangedBowAttackGoal((Mob) entity, 1.0D, 20, 15.0F));
		}
		*/
	}
}
