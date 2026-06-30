
package net.mcreator.er.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class GeoVisionItem extends Item {
	public GeoVisionItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	}
}
