package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.wither.er.item.morabag.MoraValueListener;
import net.wither.er.outcrop.OutcropWaveDataListener;
import net.wither.er.recipe.ascension.AscensionRecipeListener;
import net.wither.er.recipe.converting.AlchemyConvertingRecipeListener;

@EventBusSubscriber(modid = ErMod.MODID )
public class ReloadListenersRegister {
    @SubscribeEvent
    public static void registerReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new OutcropWaveDataListener());
        event.addListener(new AscensionRecipeListener());
        event.addListener(new AlchemyConvertingRecipeListener());
        event.addListener(new MoraValueListener());
    }
}
