package net.mcreator.er.procedures;

import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModMobEffects;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import net.wither.er.network.ErItemVariables;

@Mod.EventBusSubscriber
public class PlayerAdvancementProcedure {
    private final static ResourceLocation taskReward = new ResourceLocation("er:advancement/advancement_task_reward");
    private final static ResourceLocation goalkReward = new ResourceLocation("er:advancement/advancement_goal_reward");
    private final static ResourceLocation challengeReward = new ResourceLocation("er:advancement/advancement_challenge_reward");

	@SubscribeEvent
	public static void onAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
		Player player = event.getEntity();
		Advancement adv = event.getAdvancement();
		if (player instanceof ServerPlayer player1  && adv.getDisplay() != null && !adv.getDisplay().isHidden() && player1.getCapability(ErItemVariables.PLAYER_VARIABLES).orElse(new ErItemVariables.PlayerVariables()).Vision != ItemStack.EMPTY) {
			FrameType type = adv.getDisplay().getFrame();
			if (type == FrameType.TASK)
				giveLootTableItems(player1, taskReward);
			else if (type == FrameType.GOAL)
				giveLootTableItems(player1, goalkReward);
			else if (type == FrameType.CHALLENGE)
				giveLootTableItems(player1, challengeReward);
			if (!(player.hasEffect(ErModMobEffects.VISION_COOL_DOWN.get())) && Math.random() <= 0.15) {
				player.addEffect(new MobEffectInstance(ErModMobEffects.VISION_COOL_DOWN.get(), 60, 0, false, false));
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
		for (ItemStack itemstackiterator : level.getServer().getLootData().getLootTable(lootTableId).getRandomItems(params)) {
			ItemHandlerHelper.giveItemToPlayer(player, itemstackiterator);
		}
	}
}