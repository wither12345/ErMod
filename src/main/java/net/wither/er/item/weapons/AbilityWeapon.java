package net.wither.er.item.weapons;

import net.minecraft.world.item.Item;
import net.wither.er.item.data.weapon.WeaponAbility;

public interface AbilityWeapon {
    WeaponAbility getAbility() ;
    Item getRefinementItem();
}
