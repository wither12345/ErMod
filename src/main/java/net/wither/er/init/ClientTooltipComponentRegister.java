package net.wither.er.init;

import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.item.morabag.ClientMoraBagTooltip;
import net.wither.er.item.morabag.MoraBagComponent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientTooltipComponentRegister {
    @SubscribeEvent
    public static void register(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(MoraBagComponent.class, ClientMoraBagTooltip::new);
    }
}
