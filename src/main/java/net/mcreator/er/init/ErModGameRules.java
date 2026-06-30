/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.GameRules;

@EventBusSubscriber
public class ErModGameRules {
	public static GameRules.Key<GameRules.BooleanValue> RUNNING_STAMINA_CONSUMABLE;

	@SubscribeEvent
	public static void registerGameRules(FMLCommonSetupEvent event) {
		RUNNING_STAMINA_CONSUMABLE = GameRules.register("runningStaminaConsumable", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
	}
}