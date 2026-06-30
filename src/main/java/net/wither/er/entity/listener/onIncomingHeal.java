package net.wither.er.entity.listener;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class onIncomingHeal {
    @SubscribeEvent
    public static void onHeal(LivingHealEvent event){
        LivingEntity entity = event.getEntity();
        if(entity.getAttributes().hasAttribute(ErModAttributes.INCOMING_HEALING_BONUS.get()))
            event.setAmount(event.getAmount() * (float)entity.getAttributeValue(ErModAttributes.INCOMING_HEALING_BONUS.get()));
    }
}
