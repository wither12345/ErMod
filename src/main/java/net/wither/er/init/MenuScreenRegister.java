package net.wither.er.init;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.wither.er.client.gui.*;

@EventBusSubscriber(value = Dist.CLIENT)
public class MenuScreenRegister{
    @SubscribeEvent
    public static void loadScreen(RegisterMenuScreensEvent event){
        event.register(ErMenus.WEAPON_ENHANCE_GUI.get(), WeaponEnhanceGuiScreen::new);
        event.register(ErMenus.ARTIFACT_TRANSMUTER_GUI.get(), ArtifactTransmuterGuiScreen::new);
        event.register(ErMenus.ALCHEMY_GUI.get(), AlchemyGuiScreen::new);
        event.register(ErMenus.LEY_LINE_MAP.get(), LeyLineMapGuiScreen::new);
        event.register(ErMenus.EQUIPMENT.get(), ErEquipmentGUIScreen::new);
    }
}
