package net.mcreator.er.procedures;

import net.neoforged.neoforge.event.entity.living.LivingSwapItemsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.er.init.ErModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
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
