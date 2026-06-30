package net.wither.er.item.weapons;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public enum ErTiers implements Tier {
    STAR_1(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 250, 2.0F, 0.0F, 10),
    STAR_2(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 250, 2.0F, 1.5F, 15),
    STAR_3(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 250, 2.0F, 3F, 18);

    private final TagKey<Block> incorrectBlocksForDrops;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;

    ErTiers(TagKey<Block> key, int uses, float speed, float damage, int enchantmentValue) {
        this.incorrectBlocksForDrops = key;
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

    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public @NotNull Ingredient getRepairIngredient() {
        return Ingredient.of();
    }
}
