package net.mcreator.er.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ElementalMasterEnchantment extends Enchantment {
	public ElementalMasterEnchantment() {
		this(EquipmentSlot.MAINHAND);
	}

	private ElementalMasterEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, slots);
	}

	@Override
	public int getMinCost(int level) {
		return 1 + level * 10;
	}

	@Override
	public int getMaxCost(int level) {
		return 6 + level * 10;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem || super.canEnchant(itemStack);
    }
}