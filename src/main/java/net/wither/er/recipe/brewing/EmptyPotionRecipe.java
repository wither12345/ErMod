package net.wither.er.recipe.brewing;

import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModPotions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class EmptyPotionRecipe implements IBrewingRecipe {
    private final Item potIngredient;
    private final Potion pot;

    public EmptyPotionRecipe(Item ingredient, Potion pot) {
        super();
        this.potIngredient = ingredient;
        this.pot = pot;
    }


    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(new EmptyPotionRecipe(ErModItems.BUTTERFLY_WINGS.get(), ErModPotions.EMPTY_RESISTANCE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new EmptyPotionRecipe(ErModItems.FROG.get(), ErModPotions.EMPTY_DAMAGE_POTION_TYPE_1.get()));
            BrewingRecipeRegistry.addRecipe(new EmptyPotionRecipe(ErModItems.LIZARD_TAIL.get(), ErModPotions.EMPTY_DAMAGE_POTION_TYPE_2.get()));
        });
    }


    @Override
    public boolean isInput(ItemStack input) {
        Item inputItem = input.getItem();
        Potion pot = PotionUtils.getPotion(input);
        return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION) && pot == Potions.WATER;
    }

    @Override
    public boolean isIngredient(@NotNull ItemStack ingredient) {
        return Ingredient.of(new ItemStack(potIngredient)).test(ingredient);
    }

    @Override
    public @NotNull ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack ingredient) {
        if (isInput(input) && isIngredient(ingredient)) {
            return PotionUtils.setPotion(new ItemStack(Items.POTION), pot);
        }
        return ItemStack.EMPTY;
    }
}
