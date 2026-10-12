package net.wither.er.item.weapons;

import net.mcreator.er.init.ErModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public enum ElementalTiers implements Tier {
    ANEMO(Ingredient.of(ErModItems.DANDELION_SEED)),
    HYDRO(Ingredient.of(ErModItems.LOTUS_HEAD)),
    ELECTRO(Ingredient.of(ErModItems.ELECTRO_CRYSTAL)),
    DENDRO(Ingredient.of(ErModItems.SUMERU_ROSE)),
    PYRO(Ingredient.of(ErModItems.FLAMING_FLOWER_STAMEN)),
    CRYO(Ingredient.of(ErModItems.MIST_FLOWER_COROLLA)),
    GEO(Ingredient.of(ErModItems.COR_LAPIS));

    //private final LazyLoadedValue<Ingredient> repairIngredient;
    private final Ingredient repairIngredient;
    private final static TagKey<Block> incorrectBlocks = BlockTags.INCORRECT_FOR_IRON_TOOL;

    ElementalTiers(Ingredient repairIngredient) {
        this.repairIngredient = repairIngredient;
    }

    public int getUses() {
        return 250;
    }

    public float getSpeed() {
        return 6;
    }

    public float getAttackDamageBonus() {
        return 2;
    }

    @Override
    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlocks;
    }

    public int getLevel() {
        return 2;
    }

    public int getEnchantmentValue() {
        return 14;
    }

    public @NotNull Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }
}
