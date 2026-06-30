package net.mcreator.er.item;

import net.mcreator.er.init.ErModItems;
import net.mcreator.er.procedures.MoraBagUseProcedure;
import net.mcreator.er.procedures.MoraBag_CounterProcedure;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class MoraBagItem extends Item {
	public MoraBagItem() {
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		Entity entity = itemstack.getEntityRepresentation() != null ? itemstack.getEntityRepresentation() : Minecraft.getInstance().player;
		String hoverText = MoraBag_CounterProcedure.execute(itemstack);
		if (hoverText != null) {
			for (String line : hoverText.split("\n")) {
				list.add(Component.literal(line));
			}
		}
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack bag, Slot slot, ClickAction action, Player player) {
		if (bag.getCount() != 1 || action != ClickAction.SECONDARY) {
			return false;
		}
		else {
			ItemStack itemstack = slot.getItem();
			int mora_count = bag.getOrCreateTag().getInt("moras");
			if (itemstack.isEmpty()) {
				this.playRemoveOneSound(player);
				ItemStack itemstack1 = new ItemStack(ErModItems.MORA.get() ,Math.min(mora_count,64));
				final int count = mora_count - itemstack1.getCount();
				slot.safeInsert(itemstack1);
				bag.getOrCreateTag().putInt("moras", count);
			}
			else if (itemstack.getItem() == ErModItems.MORA.get()) {
				final int count = itemstack.getCount() + mora_count;
				slot.remove(itemstack.getCount());
				bag.getOrCreateTag().putInt("moras", count);
				this.playInsertSound(player);
			}
			else if (itemstack.getItem() == ErModItems.A_BAG_OF_MORA.get()) {
				final int count = itemstack.getOrCreateTag().getInt("moras") + mora_count;
				slot.remove(itemstack.getCount());
				bag.getOrCreateTag().putInt("moras", count);
				this.playInsertSound(player);
			}
			return true;
		}
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack bag, ItemStack input_item, Slot slot, ClickAction action, Player player, SlotAccess access) {
		if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
			int mora_count = bag.getOrCreateTag().getInt("moras");
			if (input_item.isEmpty()) {
				ItemStack itemstack = new ItemStack(ErModItems.MORA.get() ,Math.min(mora_count,64));
				final int count = bag.getOrCreateTag().getInt("moras") - itemstack.getCount();
				bag.getOrCreateTag().putInt("moras", count);
				this.playRemoveOneSound(player);
				access.set(itemstack);
			} else if(input_item.getItem() == ErModItems.MORA.get()){
				final int count = input_item.getCount() + mora_count;
				bag.getOrCreateTag().putInt("moras", count);
				input_item.setCount(0);
				this.playInsertSound(player);
			}else if(input_item.getItem() == ErModItems.A_BAG_OF_MORA.get()){
				final int count = input_item.getOrCreateTag().getInt("moras") + mora_count;
				bag.getOrCreateTag().putInt("moras", count);
				input_item.setCount(0);
				this.playInsertSound(player);
			}
			return true;
		} else {
			return false;
		}
	}

	private void playRemoveOneSound(Entity player) {
		player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
	}

	private void playInsertSound(Entity player) {
		player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
	}

	private void playDropContentsSound(Entity player) {
		player.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		MoraBagUseProcedure.execute(entity, ar.getObject());
		return ar;
	}
}