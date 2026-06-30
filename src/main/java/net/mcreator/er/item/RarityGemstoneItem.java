package net.mcreator.er.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.entity.player.Player;

import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import org.jetbrains.annotations.NotNull;

public class RarityGemstoneItem extends Item {
	public RarityGemstoneItem() {
		super(new Item.Properties().stacksTo(8));
	}

	@Override
	public boolean overrideStackedOnOther(@NotNull ItemStack gemstone, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
		if (action != ClickAction.SECONDARY) {
			return false;
		} else {
			ItemStack itemstack = slot.getItem();
			ArtifactData data = itemstack.get(DataComponentsRegister.ARTIFACT.get());
			if(data == null)
				return false;
			itemstack.update(DataComponentsRegister.ARTIFACT.get(),data,d -> d.setRarity(gemstone.getCount())) ;
			gemstone.setCount(0);
			return true;
		}
	}
}