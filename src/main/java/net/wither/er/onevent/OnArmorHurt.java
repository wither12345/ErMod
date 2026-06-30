package net.wither.er.onevent;

public class OnArmorHurt {
    /*
    @SubscribeEvent
    public static void onArmorHurt(ArmorHurtEvent event) {
        Map<EquipmentSlot, ArmorHurtEvent.ArmorEntry> armorMap = event.getArmorMap();
        Set<EquipmentSlot> ketSet = armorMap.keySet();
        for (EquipmentSlot slot : ketSet) {
            ItemStack stack = event.getArmorItemStack(slot);
            if (DataComponentsRegister.WEAPON_LEVEL.itemHas(stack)) {
                int level = stack.getComponents().get(DataComponentsRegister.WEAPON_LEVEL).level();
                event.setNewDamage(slot, event.getOriginalDamage(slot) / (1 + level / 10f));
            }
        }
    }

     */
}