
package net.mcreator.er.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import static net.mcreator.er.item.LeyLineMapItem.create;
import static net.minecraft.sounds.SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT;
import static net.minecraft.stats.Stats.ITEM_USED;
import static net.minecraft.world.InteractionResultHolder.consume;
import static net.minecraft.world.item.MapItem.getSavedData;

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
            itemstack.shrink(1);
            p_41146_.awardStat(ITEM_USED.get(this));
            p_41146_.level().playSound(null, p_41146_, UI_CARTOGRAPHY_TABLE_TAKE_RESULT, p_41146_.getSoundSource(), 1.0F, 1.0F);
            ItemStack itemstack1 = create(p_41145_, p_41146_.getBlockX(), p_41146_.getBlockZ(), (byte) 4, true, false);
            MapItemSavedData data = getSavedData(itemstack1, p_41146_.level());
            itemstack1.getOrCreateTag().putInt("centerX", data.centerX);
            itemstack1.getOrCreateTag().putInt("centerZ", data.centerZ);
            if (itemstack.isEmpty()) {
                return consume(itemstack1);
            } else {
                if (!p_41146_.getInventory().add(itemstack1.copy())) {
                    p_41146_.drop(itemstack1, false);
                }
                return consume(itemstack);
            }
        }
	}
}
