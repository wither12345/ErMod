package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.outcrop.OutcropWaveDataListener;
import net.wither.er.recipe.ascension.AscensionRecipeListener;
import net.wither.er.recipe.converting.AlchemyConvertingRecipeListener;

@Mod.EventBusSubscriber(modid = ErMod.MODID , bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReloadListenersRegister {
    @SubscribeEvent
    public static void registerReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new OutcropWaveDataListener());
        event.addListener(new AscensionRecipeListener());
        event.addListener(new AlchemyConvertingRecipeListener());
    }
}
