package net.mcreator.er.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HardEnchantment extends Enchantment {
	public HardEnchantment() {
		this(EquipmentSlot.CHEST);
	}

	private HardEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_CHEST, slots);
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

	@Override
	protected boolean checkCompatibility(@NotNull Enchantment enchantment) {
		return super.checkCompatibility(enchantment) && !List.of(Enchantments.ALL_DAMAGE_PROTECTION).contains(enchantment);
	}
}