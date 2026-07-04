package net.wither.er.player;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import net.wither.er.init.DataComponentsRegister;

import java.util.Map;
import java.util.Set;

@EventBusSubscriber
public class OnArmorHurt {
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
}