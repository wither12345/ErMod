package net.wither.er.onevent;

import net.mcreator.er.init.ErModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OnEntityDropItem {
    @SubscribeEvent
    public static void onPickupXp(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity instanceof Frog){
            event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ErModItems.FROG.get())));
        }
    }
}
