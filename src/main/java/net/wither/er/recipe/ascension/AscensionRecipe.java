package net.wither.er.recipe.ascension;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

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
        private final int mora;
        private final Input[] inputs = new Input[4] ;

        private Single(JsonElement element){
            this.mora = element.getAsJsonObject().get("mora").getAsInt() ;
            JsonArray ingredients = element.getAsJsonObject().getAsJsonArray("ingredient") ;
            int i = 0 ;
            for(JsonElement ingredient : ingredients){
                if(i >= 4)
                    break ;
                inputs[i ++] = new Input(ingredient);
            }
        }

        @Nullable
        public Input getInput(int i){
            return inputs[i] ;
        }

        public int getMora() {
            return mora;
        }
    }

    public static class Input{
        private final Item item ;
        private final int count ;

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
