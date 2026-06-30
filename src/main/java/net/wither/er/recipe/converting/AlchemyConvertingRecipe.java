package net.wither.er.recipe.converting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Set;

public record AlchemyConvertingRecipe(Item mainMaterial, int count, Set<Item> convertibles, ArrayList<ItemStack> results){
    public boolean has(Item item){
        return convertibles.contains(item);
    }

    public void addItem(final Set<Item> newSet){
        for(Item item: newSet){
            results.add(new ItemStack(item));
            convertibles.add(item);
        }
    }

    public boolean test(NonNullList<Slot> slots){
        return slots.get(5).getItem().getCount() >= count && slots.get(5).getItem().getItem() == mainMaterial && convertibles.contains(slots.get(2).getItem().getItem());
    }

    public int getSize(){
        return results.size();
    }

    public ItemStack getItemStack(int i){
        return results.get(i).copy();
    }
}
