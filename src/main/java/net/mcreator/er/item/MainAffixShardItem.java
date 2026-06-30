package net.mcreator.er.item;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import net.minecraft.client.Minecraft;

import net.mcreator.er.procedures.AffixShard_DescriptionProcedure;
import net.mcreator.er.init.ErModItems;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.artifactdata.MainAffix;
import net.wither.er.item.data.artifactdata.MinorAffix;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainAffixShardItem extends Item {
	public MainAffixShardItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).component(DataComponentsRegister.ARTIFACT_MAIN.get(), new MainAffix(Attributes.MAX_HEALTH,0,false)));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
		MainAffix affix = itemstack.get(DataComponentsRegister.ARTIFACT_MAIN.get());
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
			MainAffix affix = affixItem.get(DataComponentsRegister.ARTIFACT_MAIN.get());
			if(data == null || affix == null)
				return false;
			itemstack.update(DataComponentsRegister.ARTIFACT.get(),data,d -> d.setMain(affix)) ;
			affixItem.setCount(0);
			return true;
		}
	}
}