package net.mcreator.er.item;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.wither.er.item.IProjectileWeapon;

public class PolarStarItem extends BowItem {
	public PolarStarItem() {
		super(new Item.Properties().durability(384).rarity(Rarity.COMMON));
        ((IProjectileWeapon)this).er$setDamage(7);
	}
}