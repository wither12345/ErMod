package net.mcreator.er.item;

import net.mcreator.er.StellaFortunas;
import net.mcreator.er.init.ErModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.wither.er.network.ErItemVariables;

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
		ErItemVariables.PlayerVariables _vars = player.getCapability(ErItemVariables.PLAYER_VARIABLES).orElse(new ErItemVariables.PlayerVariables());
		if (_vars.Stella_Fortuna.getItem() instanceof StellaFortunas && stack.getCount() > 0) {
			if(_vars.Stella_Fortuna.getOrCreateTag().getInt("level") + 1 >= StellaFortunas.getMaxLevel(_vars.Stella_Fortuna.getOrCreateTag().getInt("ascension"))){
				return false ;
			}
			if(player instanceof ServerPlayer serverPlayer && !(serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE)) {
				int mora = 0;
				if (player.getCapability(ForgeCapabilities.ITEM_HANDLER, null) instanceof IItemHandlerModifiable modHandlerIter) {
					int solts = modHandlerIter.getSlots();
					for (int _idx = 0; _idx < solts && mora < this.value * 5; _idx++) {
						ItemStack iterator = modHandlerIter.getStackInSlot(_idx);
						if (iterator.getItem() == ErModItems.MORA.get())
							mora += iterator.getCount();
						else if (iterator.getItem() == ErModItems.MORA_BAG.get())
							mora += iterator.getOrCreateTag().getInt("moras");
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
							int inv = iterator.getOrCreateTag().getInt("moras");
							if (inv >= mora) {
								final int rest = inv - mora;
								iterator.getOrCreateTag().putInt("moras", rest);
								mora = 0;
							} else {
								mora -= inv;
								iterator.getOrCreateTag().putInt("moras", 0);
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