package net.wither.er.player;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.wither.er.block.entity.LinkMechanismBaseEntity;
import net.wither.er.client.renderer.block.LinkMechanismBaseRenderer;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.item.artifact_effect.ArtifactEffectRegistry;
import net.wither.er.item.morabag.MoraBagItemPlus;
import net.wither.er.network.LineMechanismMessage;
import net.wither.er.network.MoraSelectData;

import java.util.List;

@Mod.EventBusSubscriber(modid = ErMod.MODID)
public class OnEvents {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity() ;
        if(event.getLevel().isClientSide()) return;
        if(player instanceof ErEntityInterface entityInterface && entityInterface.er$getArtifactEffectLevel(ArtifactEffectRegistry.ADVENTURER.get()) > 3) {
            BlockEntity entity = event.getLevel().getBlockEntity(event.getPos());
            if (entity instanceof Container && !entity.getPersistentData().contains("ErOpened")) {
                player.addEffect(new MobEffectInstance(ErModMobEffects.ADVENTURE_HEALING.get(), 100, 0));
                entity.getPersistentData().putBoolean("ErOpened", true);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(BlockEvent.EntityPlaceEvent event) {
        if(event.getLevel().isClientSide()) return;
        BlockEntity entity = event.getLevel().getBlockEntity(event.getPos());
        if (entity != null && !entity.getPersistentData().contains("ErOpened")) {
            entity.getPersistentData().putBoolean("ErOpened", true);
        }
    }

    @SubscribeEvent
    public static void onClick(InputEvent.MouseButton.Pre event){
        LinkMechanismBaseEntity base = LinkMechanismBaseRenderer.closestBase;
        if(event.getAction() == 1 &&
                base != null &&
                base.cosToPlayer >= 0.95 &&
                Minecraft.getInstance().screen == null) {
            BlockPos pos = base.getBlockPos();
            ErMod.PACKET_HANDLER.sendToServer(new LineMechanismMessage(pos));
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(ScreenEvent.MouseScrolled.Pre event){
        List<MoraBagItemPlus.MoraVal> moraVals = MoraBagItemPlus.getVals();
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof AbstractContainerScreen<?> containerScreen) {
            ErPlayerInterface playerInterface = (ErPlayerInterface)Minecraft.getInstance().player;
            Slot slot = containerScreen.getSlotUnderMouse();
            if (slot != null && slot.isActive() && playerInterface != null) {
                ItemStack stack = slot.getItem();
                if(stack.is(ErModItems.MORA_BAG.get())){
                    int i = playerInterface.er$getMoraIndex() + (event.getScrollDelta() > 0 ? -1 : 1);
                    if(i >= moraVals.size())
                        i = 0;
                    if(i < 0)
                        i = moraVals.size() - 1;
                    playerInterface.er$setMoraIndex(i);
                    ErMod.PACKET_HANDLER.send(PacketDistributor.SERVER.noArg(), new MoraSelectData(i));
                    event.setCanceled(true);
                }
            }
        }
    }
}
