package net.wither.er.init;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.wither.er.item.morabag.ClientMoraBagTooltip;
import net.wither.er.item.morabag.MoraBagComponent;

@EventBusSubscriber
public class ClientTooltipComponentRegister {
    @SubscribeEvent
    public static void register(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(MoraBagComponent.class, ClientMoraBagTooltip::new);
    }
}
