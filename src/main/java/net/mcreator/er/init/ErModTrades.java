/*
*	MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.er.init;

import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.npc.VillagerProfession;

@EventBusSubscriber
public class ErModTrades {
	@SubscribeEvent
	public static void registerWanderingTrades(WandererTradesEvent event) {
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 5), new ItemStack(ErModItems.MORA.get()), new ItemStack(ErModItems.IRON_CHUNK.get(), 5), 50, 5, 0f));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 10), new ItemStack(ErModItems.MORA.get(), 2), new ItemStack(ErModItems.WHITE_IRON_CHUNK.get(), 5), 40, 5, 0f));
		event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 20), new ItemStack(ErModItems.MORA.get(), 3), new ItemStack(ErModItems.CRYSTAL_CHUNK.get(), 6), 25, 5, 0.05f));
	}

	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event) {
		if (event.getType() == VillagerProfession.FLETCHER) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3), new ItemStack(ErModItems.MORA.get(), 5), new ItemStack(ErModItems.HUNTERS_BOW.get()), 2, 5, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(Blocks.EMERALD_BLOCK, 32), new ItemStack(ErModItems.MORA.get(), 48), new ItemStack(ErModItems.POLAR_STAR.get()), 2, 80, 0.05f));
		}
	}
}