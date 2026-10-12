package net.mcreator.er.item;

import net.wither.er.item.IProjectileWeapon;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BowItem;

public class HuntersBowItem extends BowItem {
	public HuntersBowItem() {
		super(new Item.Properties().durability(384).rarity(Rarity.COMMON));
        ((IProjectileWeapon)this).er$setDamage(3);
	}
}