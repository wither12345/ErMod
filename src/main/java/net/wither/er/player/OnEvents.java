package net.wither.er.player;

import net.mcreator.er.StellaFortunas;
import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.block.entity.LinkMechanismBaseEntity;
import net.wither.er.client.renderer.block.LinkMechanismBaseRenderer;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.artifact_effect.ArtifactEffectRegistry;
import net.wither.er.item.morabag.MoraBagItemPlus;
import net.wither.er.network.LineMechanismMessage;
import net.wither.er.network.MoraSelectData;

import java.util.List;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber
public class OnEvents {
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

    @SubscribeEvent
    public static void onArmorHurt(ArmorHurtEvent event) {
        Map<EquipmentSlot, ArmorHurtEvent.ArmorEntry> armorMap = event.getArmorMap();
        Set<EquipmentSlot> ketSet = armorMap.keySet();
        for (EquipmentSlot slot : ketSet) {
            ItemStack stack = event.getArmorItemStack(slot);
            if (stack.getComponents().has(DataComponentsRegister.WEAPON_LEVEL.get())) {
                int level = stack.getComponents().get(DataComponentsRegister.WEAPON_LEVEL.get()).level();
                event.setNewDamage(slot, event.getOriginalDamage(slot) / (1 + level / 10f));
            }
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
            PacketDistributor.sendToServer(new LineMechanismMessage(pos));
        }
    }

    @SubscribeEvent
    public static void onPickupXp(PlayerXpEvent.PickupXp event) {
        Player player = event.getEntity();
        StellaFortunas.addExptoPlayer(player ,event.getOrb().getValue());
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity() ;
        if(event.getLevel().isClientSide()) return;
        if(player instanceof ErEntityInterface entityInterface && entityInterface.er$getArtifactEffectLevel(ArtifactEffectRegistry.ADVENTURER) > 3) {
            BlockEntity entity = event.getLevel().getBlockEntity(event.getPos());
            if (entity instanceof Container && !entity.getPersistentData().contains("ErOpened")) {
                player.addEffect(new MobEffectInstance(ErModMobEffects.ADVENTURE_HEALING, 100, 0));
                entity.getPersistentData().putBoolean("ErOpened", true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if(event.getLevel().isClientSide()) return;
        BlockEntity entity = event.getLevel().getBlockEntity(event.getPos());
        if (entity != null && !entity.getPersistentData().contains("ErOpened")) {
            entity.getPersistentData().putBoolean("ErOpened", true);
        }
    }
}
