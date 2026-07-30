package net.wither.er.player;

import net.mcreator.er.init.ErModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.item.morabag.MoraBagItemPlus;
import net.wither.er.network.MoraSelectData;

import java.util.List;

@EventBusSubscriber
public class MoraBagScroll {

    @SubscribeEvent
    public static void onMouseScroll(ScreenEvent.MouseScrolled.Pre event){
        List<MoraBagItemPlus.MoraVal> moraVals = MoraBagItemPlus.getVals();
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof AbstractContainerScreen<?> containerScreen) {
            ErPlayerInterface playerInterface = (ErPlayerInterface)Minecraft.getInstance().player;
            Slot slot = containerScreen.getSlotUnderMouse();
            if (slot != null && slot.isActive() && playerInterface != null) {
                ItemStack stack = slot.getItem();
                if(stack.is(ErModItems.MORA_BAG)){
                    int i = playerInterface.er$getMoraIndex() + (event.getScrollDeltaY() > 0 ? -1 : 1);
                    if(i >= moraVals.size())
                        i = 0;
                    if(i < 0)
                        i = moraVals.size() - 1;
                    playerInterface.er$setMoraIndex(i);
                    PacketDistributor.sendToServer(new MoraSelectData(i));
                    event.setCanceled(true);
                }
            }
        }
    }

}
