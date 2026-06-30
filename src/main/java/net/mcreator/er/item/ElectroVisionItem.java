
package net.mcreator.er.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class ElectroVisionItem extends Item {
	public ElectroVisionItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	}
}
