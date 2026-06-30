package net.mcreator.er.enchantment;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.er.init.ErModEnchantments;

import java.util.List;

public class GeoInfusionEnchantmentEnchantment extends Enchantment {
	private static final EnchantmentCategory ENCHANTMENT_CATEGORY = EnchantmentCategory.create("er_geo_infusion_enchantment", item -> Ingredient.of(ItemTags.create(new ResourceLocation("enchantable/weapon"))).test(new ItemStack(item)));

	public GeoInfusionEnchantmentEnchantment() {
		this(EquipmentSlot.MAINHAND);
	}

	private GeoInfusionEnchantmentEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.VERY_RARE, ENCHANTMENT_CATEGORY, slots);
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
		return 3;
	}

	@Override
	protected boolean checkCompatibility(Enchantment enchantment) {
		return super.checkCompatibility(enchantment) && !List.of(ErModEnchantments.ELECTRO_INFUSION_ENCHANTMENT.get(), ErModEnchantments.ANEMO_INFUSION_ENCHANTMENT.get(), ErModEnchantments.CRYO_INFUSION_ENCHANTMENT.get(),
				ErModEnchantments.DENDRO_INFUSION_ENCHANTMENT.get(), ErModEnchantments.HYDRO_INFUSION_ENCHANTMENT.get(), ErModEnchantments.PYRO_INFUSION_ENCHANTMENT.get()).contains(enchantment);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}
}