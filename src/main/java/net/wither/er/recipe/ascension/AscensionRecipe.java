package net.wither.er.recipe.ascension;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AscensionRecipe {
    private final Single[] singles = new Single[6] ;

    public AscensionRecipe(JsonArray ascensionArray){
        int i = 0 ;
        for(JsonElement element : ascensionArray){
            if(i >= 6)
                break ;
            singles[i ++] = new Single(element);
        }
    }

    public Single getRecipe(int i){
        if(i < 6)
            return singles[i] ;
        return null ;
    }

    public static class Single{
        final int mora;
        final Input[] inputs = new Input[3] ;

        private Single(JsonElement element){
            this.mora = element.getAsJsonObject().get("mora").getAsInt() ;
            JsonArray ingredients = element.getAsJsonObject().getAsJsonArray("ingredient") ;
            int i = 0 ;
            for(JsonElement ingredient : ingredients){
                if(i >= 3)
                    break ;
                inputs[i ++] = new Input(ingredient);
            }
        }

        public Input getInput(int i){
            return inputs[i] ;
        }

        public int getMora() {
            return mora;
        }
    }

    public static class Input{
        final Item item ;
        final int count ;

        private Input(JsonElement element){
            String itemId = element.getAsJsonObject().get("item").getAsString() ;
            this.item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId)) ;
            if(element.getAsJsonObject().has("count"))
                this.count = element.getAsJsonObject().get("count").getAsInt() ;
            else
                this.count = 1 ;
        }

        public boolean match(ItemStack stack){
            return stack.getItem() == this.item && stack.getCount() >= this.count ;
        }

        public Item getItem() {
            return item;
        }

        public int getCount() {
            return count;
        }
    }
}
