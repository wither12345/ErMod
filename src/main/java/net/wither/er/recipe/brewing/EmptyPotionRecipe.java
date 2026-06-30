package net.wither.er.recipe.brewing;

import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModPotions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Optional;

@EventBusSubscriber
public class EmptyPotionRecipe implements IBrewingRecipe {
    private final DeferredHolder<Item, Item> potIngredient;
    private final DeferredHolder<Potion, Potion> pot;

    public EmptyPotionRecipe(DeferredHolder<Item, Item> ingredient, DeferredHolder<Potion, Potion> pot) {
        super();
        this.potIngredient = ingredient;
        this.pot = pot;
    }

    @SubscribeEvent
    public static void init(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(new EmptyPotionRecipe(ErModItems.BUTTERFLY_WINGS, ErModPotions.EMPTY_RESISTANCE_POTION));
        event.getBuilder().addRecipe(new EmptyPotionRecipe(ErModItems.FROG, ErModPotions.EMPTY_DAMAGE_POTION_TYPE_1));
        event.getBuilder().addRecipe(new EmptyPotionRecipe(ErModItems.LIZARD_TAIL, ErModPotions.EMPTY_DAMAGE_POTION_TYPE_2));
    }

    @Override
    public boolean isInput(ItemStack input) {
        Item inputItem = input.getItem();
        Optional<Holder<Potion>> optionalPotion = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();
        return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION) && optionalPotion.isPresent() && optionalPotion.get().is(Potions.WATER);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return Ingredient.of(new ItemStack(potIngredient)).test(ingredient);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (isInput(input) && isIngredient(ingredient)) {
            return PotionContents.createItemStack(input.getItem(), pot);
        }
        return ItemStack.EMPTY;
    }
}
