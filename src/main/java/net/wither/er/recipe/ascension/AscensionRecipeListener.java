package net.wither.er.recipe.ascension;

import com.google.gson.*;
import net.mcreator.er.ErMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AscensionRecipeListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).create();
    private static final HashMap<Item, AscensionRecipe> ascensionRecipeMap = new HashMap<>();

    public AscensionRecipeListener() {
        super(GSON, "ascension/weapon");
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager manager, @NotNull ProfilerFiller filler) {
        map.forEach((location, element) -> {
            try {
                read(element.getAsJsonObject());
            } catch (Exception var6) {
                ErMod.LOGGER.error("Parsing error loading custom weapon ascension {}: {}", location, var6.getMessage());
            }
        });
    }

    private static void read(JsonObject object){
        ArrayList<Item> items = new ArrayList<>() ;
        if(object.has("item"))
            items.add(BuiltInRegistries.ITEM.get(ResourceLocation.parse(object.get("item").getAsString())));
        else{
            JsonArray itemsArray = object.getAsJsonArray("items") ;
            for(JsonElement itemElement : itemsArray){
                items.add(BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemElement.getAsString())));
            }
        }
        JsonArray ascensionArray = object.getAsJsonArray("ascension");
        AscensionRecipe recipe = new AscensionRecipe(ascensionArray) ;
        for(Item item : items)
            ascensionRecipeMap.put(item, recipe) ;
    }

    public static AscensionRecipe get(Item item){
        return ascensionRecipeMap.get(item) ;
    }

    public static List<AscensionEntry> getAscensionEntryList() {
        List<AscensionEntry> ret = new ArrayList<>() ;
        for(Map.Entry<Item, AscensionRecipe> entry : ascensionRecipeMap.entrySet()){
            for(int i = 0 ; i < 6 ; i ++) {
                if(entry.getValue().getRecipe(i) != null)
                    ret.add(new AscensionEntry(i, entry.getKey(), entry.getValue().getRecipe(i)));
            }
        }
        return ret ;
    }

    public record AscensionEntry(int ascension, Item key, AscensionRecipe.Single recipe){}
}