package net.mcreator.er.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.artifactdata.MinorAffix;
import org.jetbrains.annotations.NotNull;

public class MinorUpgradesItem extends Item {
	public MinorUpgradesItem() {
		super(new Item.Properties().rarity(Rarity.EPIC));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}

	@Override
	public boolean overrideStackedOnOther(@NotNull ItemStack upgradeItem, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
		if (action != ClickAction.SECONDARY) {
			return false;
		} else {
			ItemStack itemstack = slot.getItem();
			ArtifactData data = itemstack.get(DataComponentsRegister.ARTIFACT.get());
			if(data == null)
				return false;
			itemstack.update(DataComponentsRegister.ARTIFACT.get(),data,d -> d.setLevel(d.level(), upgradeItem.getCount())) ;
			upgradeItem.setCount(0);
			return true;
		}
	}
}