package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.MissingMappingsEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MissingMappingsHandler{
    private static final ResourceLocation MISSING_MEMORY_OF_ROVING_GALES = new ResourceLocation(ErMod.MODID, "memoryof_roving_gales");

    @SubscribeEvent
    public static void onMissingMappings(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Item> mapping : event.getAllMappings(Registries.ITEM)) {
            if (mapping.getKey().equals(MISSING_MEMORY_OF_ROVING_GALES)) {
                mapping.remap(ErModItems.MEMORY_OF_ROVING_GALES.get());
            }
        }
    }
}
