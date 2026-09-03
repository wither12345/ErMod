package net.mcreator.er.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.artifactdata.MinorAffix;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MinorAffixShardItem extends Item {
	public MinorAffixShardItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(@NotNull ItemStack itemstack) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@NotNull ItemStack itemstack, Item.@NotNull TooltipContext context, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
		MinorAffix affix = itemstack.get(DataComponentsRegister.ARTIFACT_MINOR.get());
		if(affix != null)
			list.add(Component.literal(affix.toString())) ;
	}

	@Override
	public boolean overrideStackedOnOther(@NotNull ItemStack affixItem, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
		if (action != ClickAction.SECONDARY) {
			return false;
		} else {
			ItemStack itemstack = slot.getItem();
			ArtifactData data = itemstack.get(DataComponentsRegister.ARTIFACT.get());
			MinorAffix affix = affixItem.get(DataComponentsRegister.ARTIFACT_MINOR.get());
			int count = affixItem.getCount();
			if(data == null || affix == null)
				return false;
			itemstack.update(DataComponentsRegister.ARTIFACT.get(),data,d -> d.addMinor(affix, count)) ;
			affixItem.setCount(0);
			return true;
		}
	}
}