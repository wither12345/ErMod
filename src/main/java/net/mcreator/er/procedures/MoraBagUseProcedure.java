package net.mcreator.er.procedures;

import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.er.init.ErModItems;

public class MoraBagUseProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double count = 0;
		if (entity.isShiftKeyDown()) {
			if (entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null) instanceof IItemHandlerModifiable _modHandlerIter) {
				for (int _idx = 0; _idx < _modHandlerIter.getSlots(); _idx++) {
					ItemStack itemStack = _modHandlerIter.getStackInSlot(_idx);
					if (itemStack.getItem() == ErModItems.MORA.get()) {
						count = count + itemStack.getCount();
						itemStack.shrink(itemStack.getCount());
					}
					if (itemStack.getItem() == ErModItems.A_BAG_OF_MORA.get()) {
						count = count + itemStack.getOrCreateTag().getInt("moras");
						itemStack.shrink(1);
					}
				}
			}
			itemstack.getOrCreateTag().putDouble("moras", (itemstack.getOrCreateTag().getDouble("moras") + count));
		} else {
			if (itemstack.getOrCreateTag().getDouble("moras") >= 64) {
				if (entity instanceof Player _player) {
					ItemStack _setstack = new ItemStack(ErModItems.MORA.get()).copy();
					_setstack.setCount(64);
					ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
				}
				itemstack.getOrCreateTag().putDouble("moras", (itemstack.getOrCreateTag().getDouble("moras") - 64));
			} else {
				if (entity instanceof Player _player) {
					ItemStack _setstack = new ItemStack(ErModItems.MORA.get()).copy();
					_setstack.setCount((int) itemstack.getOrCreateTag().getDouble("moras"));
					ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
				}
				itemstack.getOrCreateTag().putDouble("moras", 0);
			}
		}
	}
}