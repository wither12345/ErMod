package net.wither.er.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.wither.er.client.gui.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MenuScreenRegister{
    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ErMenus.WEAPON_ENHANCE_GUI.get(), WeaponEnhanceGuiScreen::new);
            MenuScreens.register(ErMenus.ARTIFACT_TRANSMUTER_GUI.get(), ArtifactTransmuterGuiScreen::new);
            MenuScreens.register(ErMenus.ALCHEMY_GUI.get(), AlchemyGuiScreen::new);
            MenuScreens.register(ErMenus.ALCHEMY_CRAFT.get(), AlchemyCraftGuiScreen::new);
            MenuScreens.register(ErMenus.LEY_LINE_MAP.get(), LeyLineMapGuiScreen::new);
            MenuScreens.register(ErMenus.EQUIPMENT.get(), ErEquipmentGUIScreen::new);
        });
    }

}
