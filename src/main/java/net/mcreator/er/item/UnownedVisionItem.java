package net.mcreator.er.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class UnownedVisionItem extends Item {
	public UnownedVisionItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	}
}