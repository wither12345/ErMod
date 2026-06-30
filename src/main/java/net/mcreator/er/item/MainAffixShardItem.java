package net.mcreator.er.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.artifactdata.MainAffix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.wither.er.init.DataComponentsRegister.ARTIFACT;

public class MainAffixShardItem extends Item {
	public MainAffixShardItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
	}

	@Override
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}

	@Override
	public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level context, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
		MainAffix affix = MainAffix.createNew(itemstack.getOrCreateTag().getString("affix"));
        list.add(Component.literal(affix.toString())) ;
    }

	@Override
	public boolean overrideStackedOnOther(@NotNull ItemStack affixItem, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
		if (action != ClickAction.SECONDARY) {
			return false;
		} else {
            ItemStack itemstack = slot.getItem();
            ArtifactData data = ARTIFACT.getData(itemstack);
			MainAffix affix = MainAffix.createNew(affixItem.getOrCreateTag().getString("affix"));
            if (data == null)
                return false;
            ARTIFACT.update(itemstack, d -> d.setMain(affix));
            affixItem.setCount(0);
            return true;
        }
	}
}