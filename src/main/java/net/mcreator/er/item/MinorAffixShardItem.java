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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.artifactdata.MinorAffix;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.wither.er.init.DataComponentsRegister.ARTIFACT;

public class MinorAffixShardItem extends Item {
	public MinorAffixShardItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		MinorAffix affix = MinorAffix.create(itemstack.getOrCreateTag().getString("affix"));
		list.add(Component.literal(affix.toString())) ;
	}

	@Override
	public boolean overrideStackedOnOther(@NotNull ItemStack affixItem, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
		if (action != ClickAction.SECONDARY) {
			return false;
		} else {
            ItemStack itemstack = slot.getItem();
            ArtifactData data = ARTIFACT.getData(itemstack);
			MinorAffix affix = MinorAffix.create(affixItem.getOrCreateTag().getString("affix"));
            int count = affixItem.getCount();
            if (data == null)
                return false;
            ARTIFACT.update(itemstack, d -> d.addMinor(affix, count));
            affixItem.setCount(0);
            return true;
        }
	}
}