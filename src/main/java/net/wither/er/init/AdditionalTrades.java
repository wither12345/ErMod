package net.wither.er.init;

import net.mcreator.er.init.ErModItems;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

@EventBusSubscriber
public class AdditionalTrades {
    @SubscribeEvent
    public static void registerWanderingTrades(WandererTradesEvent event) {
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 3), new ItemStack(ErModItems.MORA.get(), 32), new ItemStack(ErModItems.BUTTERFLY_WINGS.get()), 15, 5, 0f));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 3), new ItemStack(ErModItems.MORA.get(), 32), new ItemStack(ErModItems.FROG.get()), 15, 5, 0f));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 3), new ItemStack(ErModItems.MORA.get(), 32), new ItemStack(ErModItems.BUTTERFLY_WINGS.get()), 15, 5, 0f));
    }

    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CLERIC) {
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 2), new ItemStack(ErModItems.MORA.get(), 5), new ItemStack(ErModItems.SANCTIFYING_UNCTION.get()), 10, 3, 0.05f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 4), new ItemStack(ErModItems.MORA.get(), 5), new ItemStack(ErModItems.SANCTIFYING_ESSENCE.get()), 5, 25, 0.05f));
        }
        else if (event.getType() == VillagerProfession.WEAPONSMITH) {
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3), new ItemStack(ErModItems.MORA.get(), 5), new ItemStack(ErModItems.WASTER_GREATSWORD.get()), 2, 5, 0.05f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3), new ItemStack(ErModItems.MORA.get(), 5), new ItemStack(ErModItems.DULL_BLADE.get()), 2, 5, 0.05f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 12), new ItemStack(ErModItems.MORA.get(), 32), new ItemStack(ErModItems.SILVER_SWORD.get()), 1, 15, 0.05f));
        }
    }
}
