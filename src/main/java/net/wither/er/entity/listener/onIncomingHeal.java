package net.wither.er.entity.listener;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

@EventBusSubscriber
public class onIncomingHeal {
    @SubscribeEvent
    public static void onHeal(LivingHealEvent event){
        LivingEntity entity = event.getEntity();
        if(entity.getAttributes().hasAttribute(ErModAttributes.INCOMING_HEALING_BONUS))
            event.setAmount(event.getAmount() * (float)entity.getAttributeValue(ErModAttributes.INCOMING_HEALING_BONUS));
    }
}
