package net.mcreator.er.procedures;

import net.wither.er.network.ErItemVariables;

import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Advancement;

import net.mcreator.er.init.ErModMobEffects;
import net.mcreator.er.init.ErModItems;

@EventBusSubscriber
public class PlayerAdvancementProcedure {
	private final static ResourceLocation taskReward = ResourceLocation.parse("er:advancement/advancement_task_reward");
	private final static ResourceLocation goalkReward = ResourceLocation.parse("er:advancement/advancement_goal_reward");
	private final static ResourceLocation challengeReward = ResourceLocation.parse("er:advancement/advancement_challenge_reward");

	@SubscribeEvent
	public static void onAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
		Player player = event.getEntity();
		Advancement adv = event.getAdvancement().value();
		if (player instanceof ServerPlayer player1 && !adv.display().get().isHidden() && player1.getData(ErItemVariables.PLAYER_VARIABLES).Vision != ItemStack.EMPTY) {
			AdvancementType type = adv.display().get().getType();
			if (type == AdvancementType.TASK)
				giveLootTableItems(player1, taskReward);
			else if (type == AdvancementType.GOAL)
				giveLootTableItems(player1, goalkReward);
			else if (type == AdvancementType.CHALLENGE)
				giveLootTableItems(player1, challengeReward);
			if (!(player.hasEffect(ErModMobEffects.VISION_COOL_DOWN)) && Math.random() <= 0.15) {
				player.addEffect(new MobEffectInstance(ErModMobEffects.VISION_COOL_DOWN, 60, 0, false, false));
				player.displayClientMessage(Component.literal((Component.translatable("message.er.get_vision").getString())), false);
				ItemStack _setstack = new ItemStack(ErModItems.UNOWNED_VISION.get());
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(player, _setstack);
			}
		}
	}

	public static void giveLootTableItems(ServerPlayer player, ResourceLocation lootTableId) {
		ServerLevel level = (ServerLevel) player.level();
		LootParams.Builder paramsBuilder = new LootParams.Builder(level).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.ORIGIN, player.position()).withLuck(player.getLuck());
		LootParams params = paramsBuilder.create(LootContextParamSets.ADVANCEMENT_REWARD);
		for (ItemStack itemstackiterator : level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, lootTableId)).getRandomItems(params)) {
			ItemHandlerHelper.giveItemToPlayer(player, itemstackiterator);
		}
	}
}