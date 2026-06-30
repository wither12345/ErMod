package net.mcreator.er.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.wither.er.network.ErItemVariables;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;

import net.mcreator.er.init.ErModItems;
import net.mcreator.er.StellaFortunas;

public class ExperienceBook extends Item {
	int value;

	public ExperienceBook(Item.Properties properties, int value) {
		super(properties);
		this.value = value;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		ItemStack stack = entity.getItemInHand(hand) ;
		if (world instanceof ServerLevel level) {
			while (applyExp(stack, entity) && entity.isShiftKeyDown());
		}
		return ar;
	}

	private boolean applyExp(ItemStack stack, Player player){
		ErItemVariables.PlayerVariables _vars = player.getData(ErItemVariables.PLAYER_VARIABLES);
		if (_vars.Stella_Fortuna.getItem() instanceof StellaFortunas && stack.getCount() > 0) {
			if(_vars.Stella_Fortuna.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("level") + 1 >= StellaFortunas.getMaxLevel(_vars.Stella_Fortuna.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("ascension"))){
				return false ;
			}
			if(player instanceof ServerPlayer serverPlayer && !(serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE)) {
				int mora = 0;
				if (player.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable modHandlerIter) {
					int solts = modHandlerIter.getSlots();
					for (int _idx = 0; _idx < solts && mora < this.value * 5; _idx++) {
						ItemStack iterator = modHandlerIter.getStackInSlot(_idx);
						if (iterator.getItem() == ErModItems.MORA.get())
							mora += iterator.getCount();
						else if (iterator.getItem() == ErModItems.MORA_BAG.get())
							mora += iterator.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("moras");
					}
					if (mora < this.value * 5) {
						player.displayClientMessage(Component.literal((Component.translatable("message.er.no_enough_mora").getString())), false);
						return false;
					}
					mora = this.value * 5;
					for (int _idx = 0; _idx < solts && mora > 0; _idx++) {
						ItemStack iterator = modHandlerIter.getStackInSlot(_idx);
						if (iterator.getItem() == ErModItems.MORA.get()) {
							if (iterator.getCount() >= mora) {
								iterator.shrink(mora);
								mora = 0;
							} else {
								mora -= iterator.getCount();
								iterator.shrink(iterator.getCount());
							}
						} else if (iterator.getItem() == ErModItems.MORA_BAG.get() || iterator.getItem() == ErModItems.A_BAG_OF_MORA.get()) {
							int inv = iterator.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("moras");
							if (inv >= mora) {
								final int rest = inv - mora;
								CustomData.update(DataComponents.CUSTOM_DATA, iterator, tag -> tag.putInt("moras", rest));
								mora = 0;
							} else {
								mora -= inv;
								CustomData.update(DataComponents.CUSTOM_DATA, iterator, tag -> tag.putInt("moras", 0));
							}
						}
					}
				}
				stack.shrink(1);
			}
			StellaFortunas.addExptoPlayer(player, this.value);
			return true ;
		}
		return false ;
	}
}