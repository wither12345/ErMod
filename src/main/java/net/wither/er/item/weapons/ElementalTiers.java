package net.wither.er.item.weapons;

import net.mcreator.er.init.ErModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public enum ElementalTiers implements Tier {
    ANEMO(Ingredient.of(ErModItems.DANDELION_SEED.get())),
    HYDRO(Ingredient.of(ErModItems.LOTUS_HEAD.get())),
    ELECTRO(Ingredient.of(ErModItems.ELECTRO_CRYSTAL.get())),
    DENDRO(Ingredient.of(ErModItems.SUMERU_ROSE.get())),
    PYRO(Ingredient.of(ErModItems.FLAMING_FLOWER_STAMEN.get())),
    CRYO(Ingredient.of(ErModItems.MIST_FLOWER_COROLLA.get())),
    GEO(Ingredient.of(ErModItems.COR_LAPIS.get()));

    //private final LazyLoadedValue<Ingredient> repairIngredient;
    private final Ingredient repairIngredient;

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
