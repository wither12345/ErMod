package net.wither.er.recipe.brewing;

import net.mcreator.er.init.ErModPotions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.wither.er.elements.Element;
import net.wither.er.init.ElementalAttributesRegister;

import java.util.Optional;

@EventBusSubscriber
public class DamagePotionRecipe implements IBrewingRecipe {
    private final DeferredHolder<Item, Item> potIngredient;
    private final boolean type;
    private final DeferredHolder<Potion, Potion> pot;

    public DamagePotionRecipe(Element.Category category, DeferredHolder<Potion, Potion> pot) {
        super();
        this.potIngredient = category.getBrewIngredient();
        this.pot = pot;
        this.type = category.getDmgPotType();
    }

    @SubscribeEvent
    public static void init(RegisterBrewingRecipesEvent event) {
        for (Element.Category category : Element.Category.values()) {
            event.getBuilder().addRecipe(new DamagePotionRecipe(category, ElementalAttributesRegister.DMG_POT.get(category)));
        }
    }

    @Override
    public boolean isInput(ItemStack input) {
        Item inputItem = input.getItem();
        Optional<Holder<Potion>> optionalPotion = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();
        return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION)
                && optionalPotion.isPresent() && optionalPotion.get().is(type ? ErModPotions.EMPTY_DAMAGE_POTION_TYPE_2 : ErModPotions.EMPTY_DAMAGE_POTION_TYPE_1);
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