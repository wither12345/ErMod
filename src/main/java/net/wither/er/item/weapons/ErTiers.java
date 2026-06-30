package net.wither.er.item.weapons;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public enum ErTiers implements Tier {
    STAR_1(250, 2.0F, 0.0F, 10),
    STAR_2(250, 2.0F, 1.5F, 15),
    STAR_3(250, 2.0F, 3F, 15);

    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;

    ErTiers(int uses, float speed, float damage, int enchantmentValue) {
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public @NotNull Ingredient getRepairIngredient() {
        return Ingredient.of();
    }
}
