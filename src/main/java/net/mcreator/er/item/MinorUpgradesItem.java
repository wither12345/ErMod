package net.mcreator.er.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.wither.er.item.data.artifactdata.ArtifactData;
import org.jetbrains.annotations.NotNull;

import static net.wither.er.init.DataComponentsRegister.ARTIFACT;

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
            ArtifactData data = ARTIFACT.getData(itemstack);
            if (data == null)
                return false;
            ARTIFACT.update(itemstack, d -> d.setLevel(d.level(), upgradeItem.getCount()));
            upgradeItem.setCount(0);
            return true;
        }
	}
}