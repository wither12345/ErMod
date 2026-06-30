
package net.mcreator.er.item;

import net.mcreator.er.MultipleInfusion;
import net.mcreator.er.procedures.WoodenClubEntitySwingsItemProcedure;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class WoodenClubItem extends SwordItem implements MultipleInfusion {
	private static final Tier TOOL_TIER = new Tier() {
		@Override
		public int getUses() {
			return 250;
		}

		@Override
		public float getSpeed() {
			return 4f;
		}

		@Override
		public float getAttackDamageBonus() {
			return 0;
		}

		@Override
		public int getLevel() {
			return 0;
		}

		@Override
		public int getEnchantmentValue() {
			return 2;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of();
		}
	};

	public WoodenClubItem() {
		super(TOOL_TIER, 1, -2.4f, new Item.Properties());
	}

	@Override
	public int getInfusion(ItemStack itemstack, Entity entity) {
		if (itemstack.getOrCreateTag().getDouble("Pyro") >= 1)
			return 7;
		return 0;
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		WoodenClubEntitySwingsItemProcedure.execute(entity.level(), itemstack);
		return retval;
	}
}
