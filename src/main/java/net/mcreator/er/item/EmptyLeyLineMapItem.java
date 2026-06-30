
package net.mcreator.er.item;

import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.component.DataComponents;

public class EmptyLeyLineMapItem extends Item {
	public EmptyLeyLineMapItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level p_41145_, Player p_41146_, InteractionHand p_41147_) {
		ItemStack itemstack = p_41146_.getItemInHand(p_41147_);
		if (p_41145_.isClientSide) {
			return InteractionResultHolder.success(itemstack);
		} else {
			itemstack.consume(1, p_41146_);
			p_41146_.awardStat(Stats.ITEM_USED.get(this));
			p_41146_.level().playSound(null, p_41146_, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, p_41146_.getSoundSource(), 1.0F, 1.0F);
			ItemStack itemstack1 = LeyLineMapItem.create(p_41145_, p_41146_.getBlockX(), p_41146_.getBlockZ(), (byte) 4, true, false);
			MapItemSavedData data = MapItem.getSavedData(itemstack1, p_41146_.level());
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack1, tags -> tags.putInt("centerX", data.centerX));
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack1, tags -> tags.putInt("centerZ", data.centerZ));
			if (itemstack.isEmpty()) {
				return InteractionResultHolder.consume(itemstack1);
			} else {
				if (!p_41146_.getInventory().add(itemstack1.copy())) {
					p_41146_.drop(itemstack1, false);
				}
				return InteractionResultHolder.consume(itemstack);
			}
		}
	}
}
