/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ErModGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> RUNNING_STAMINA_CONSUMABLE = GameRules.register("runningStaminaConsumable", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
}