package net.mcreator.er.item;

import net.mcreator.er.init.ErModAttributes;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.BowInterface;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BowItem;
import net.wither.er.item.data.weapon.WeaponAttributeData;

public class PolarStarItem extends BowItem {
	public PolarStarItem() {
		super(new Item.Properties().durability(384).rarity(Rarity.COMMON).component(DataComponentsRegister.WEAPON_ATTR.get(), new WeaponAttributeData(ErModAttributes.CRIT_RATE,0.072,false)));
		if (this instanceof BowInterface bowInterface) {
			bowInterface.setDamage(7);
		}
	}
}