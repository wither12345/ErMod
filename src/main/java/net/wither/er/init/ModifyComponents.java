package net.wither.er.init;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.wither.er.item.data.weapon.WeaponAttributeData;
import net.wither.er.item.data.weapon.WeaponLevelData;

@EventBusSubscriber()
public class ModifyComponents {
    @SubscribeEvent
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        event.modifyMatching(
                ModifyComponents::hasLevelComponents,
                builder -> builder.set(DataComponentsRegister.WEAPON_LEVEL.get(), new WeaponLevelData(1,0,0,0))
        );
        event.modify(Items.DIAMOND_SWORD, builder -> builder.set(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(Attributes.ATTACK_DAMAGE,0.018,true)));
        event.modify(Items.DIAMOND_HELMET, builder -> builder.set(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(Attributes.MAX_HEALTH,0.009,true)));
        event.modify(Items.DIAMOND_CHESTPLATE, builder -> builder.set(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(Attributes.MAX_HEALTH,0.009,true)));
        event.modify(Items.DIAMOND_LEGGINGS, builder -> builder.set(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(Attributes.MAX_HEALTH,0.009,true)));
        event.modify(Items.DIAMOND_BOOTS, builder -> builder.set(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(Attributes.MAX_HEALTH,0.009,true)));

        event.modify(Items.NETHERITE_SWORD, builder -> builder.set(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(Attributes.ATTACK_DAMAGE,0.036,true)));
        event.modify(Items.NETHERITE_HELMET, builder -> builder.set(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(Attributes.MAX_HEALTH,0.018,true)));
        event.modify(Items.NETHERITE_CHESTPLATE, builder -> builder.set(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(Attributes.MAX_HEALTH,0.018,true)));
        event.modify(Items.NETHERITE_LEGGINGS, builder -> builder.set(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(Attributes.MAX_HEALTH,0.018,true)));
        event.modify(Items.NETHERITE_BOOTS, builder -> builder.set(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(Attributes.MAX_HEALTH,0.018,true)));
    }

    private static boolean hasLevelComponents(Item item){
        return item instanceof ArmorItem || item instanceof SwordItem || item instanceof BowItem;
    }
}
