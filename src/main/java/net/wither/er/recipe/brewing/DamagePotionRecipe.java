package net.wither.er.recipe.brewing;

import net.mcreator.er.init.ErModPotions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.elements.Element;
import net.wither.er.init.ElementalAttributesRegister;

@Mod.EventBusSubscriber
public class DamagePotionRecipe implements IBrewingRecipe {
    private final Item potIngredient;
    private final boolean type;
    private final Potion pot;

    public DamagePotionRecipe(Element.Category category, RegistryObject<Potion> pot) {
        super();
        this.potIngredient = category.getBrewIngredient();
        this.pot = pot.get();
        this.type = category.getDmgPotType();
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        for (Element.Category category : Element.Category.values()) {
            event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new DamagePotionRecipe(category, ElementalAttributesRegister.DMG_POT.get(category))));
        }
    }

    @Override
    public boolean isInput(ItemStack input) {
        Item inputItem = input.getItem();
        Potion pot = PotionUtils.getPotion(input);
        return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION) &&
                pot == (type ? ErModPotions.EMPTY_DAMAGE_POTION_TYPE_2.get() : ErModPotions.EMPTY_DAMAGE_POTION_TYPE_1.get());
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return Ingredient.of(new ItemStack(potIngredient)).test(ingredient);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (isInput(input) && isIngredient(ingredient)) {
            return PotionUtils.setPotion(new ItemStack(Items.POTION), pot);
        }
        return ItemStack.EMPTY;
    }
}