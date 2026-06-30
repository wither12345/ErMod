package net.mcreator.er.procedures;

import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.component.DataComponents;

import net.mcreator.er.init.ErModItems;

public class MoraBagUseProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double count = 0;
		if (entity.isShiftKeyDown()) {
			if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandlerIter) {
				for (int _idx = 0; _idx < _modHandlerIter.getSlots(); _idx++) {
					ItemStack itemstackiterator = _modHandlerIter.getStackInSlot(_idx);
					if (itemstackiterator.getItem() == ErModItems.MORA.get()) {
						count = count + itemstackiterator.getCount();
						itemstackiterator.shrink(itemstackiterator.getCount());
					}
					if (itemstackiterator.getItem() == ErModItems.A_BAG_OF_MORA.get()) {
						count = count + itemstackiterator.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("moras");
						itemstackiterator.shrink(1);
					}
				}
			}
			{
				final String _tagName = "moras";
				final double _tagValue = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("moras") + count);
				CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
			}
		} else {
			if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("moras") >= 64) {
				if (entity instanceof Player _player) {
					ItemStack _setstack = new ItemStack(ErModItems.MORA.get()).copy();
					_setstack.setCount(64);
					ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
				}
				{
					final String _tagName = "moras";
					final double _tagValue = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("moras") - 64);
					CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
				}
			} else {
				if (entity instanceof Player _player) {
					ItemStack _setstack = new ItemStack(ErModItems.MORA.get()).copy();
					_setstack.setCount((int) itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("moras"));
					ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
				}
				{
					final String _tagName = "moras";
					final double _tagValue = 0;
					CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
				}
			}
		}
	}
}