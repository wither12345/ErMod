package net.wither.er.player;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModMobEffects;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.entity.IErEntity;
import net.wither.er.item.artifact_effect.ArtifactEffectRegistry;
import net.wither.er.item.data.FortunaData;
import net.wither.er.item.fortuna.StellaFortuna;
import net.wither.er.network.ErItemVariables;

@Mod.EventBusSubscriber(modid = ErMod.MODID)
public class OnEvents {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity() ;
        if(event.getLevel().isClientSide()) return;
        if(player instanceof IErEntity entityInterface && entityInterface.er$getArtifactEffectLevel(ArtifactEffectRegistry.ADVENTURER.get()) > 3) {
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
    public static void onPickupXp(PlayerXpEvent.PickupXp event) {
        Player player = event.getEntity();
        StellaFortuna.addExpToPlayer(player ,event.getOrb().getValue());
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        ItemStack fortunaItem = player.getCapability(ErItemVariables.PLAYER_VARIABLES).map(var -> var.Stella_Fortuna).orElse(ItemStack.EMPTY);
        if(fortunaItem.getItem() instanceof StellaFortuna fortuna)
            fortuna.applyAttr(player, FortunaData.get(fortunaItem));
        player.setHealth(player.getMaxHealth());
    }
}
