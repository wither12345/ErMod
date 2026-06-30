package net.wither.er.player;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModMobEffects;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.wither.er.artifact_effect.ArtifactEffectRegistry;
import net.wither.er.entity.ErEntityInterface;

@EventBusSubscriber(modid = ErMod.MODID)
public class onOpenChest {
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
    public static void onRightClickBlock(BlockEvent.EntityPlaceEvent event) {
        if(event.getLevel().isClientSide()) return;
        BlockEntity entity = event.getLevel().getBlockEntity(event.getPos());
        if (entity != null && !entity.getPersistentData().contains("ErOpened")) {
            entity.getPersistentData().putBoolean("ErOpened", true);
        }
    }

}
