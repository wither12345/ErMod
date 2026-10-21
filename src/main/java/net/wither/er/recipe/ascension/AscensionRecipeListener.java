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
    private static final HashMap<Item, AscensionRecipe> WEAPON_ASCENSION_MAP = new HashMap<>();
    private static final HashMap<Item, AscensionRecipe> FORTUNA_ASCENSION_MAP = new HashMap<>();

    public AscensionRecipeListener() {
        super(GSON, "ascension");
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager manager, @NotNull ProfilerFiller filler) {
        WEAPON_ASCENSION_MAP.clear();
        FORTUNA_ASCENSION_MAP.clear();
        map.forEach((location, element) -> {
            try {
                read(element.getAsJsonObject());
            } catch (Exception var6) {
                ErMod.LOGGER.error("Parsing error loading custom ascension {}: {}", location, var6.getMessage());
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
        HashMap<Item, AscensionRecipe> map = WEAPON_ASCENSION_MAP;
        if(object.has("type") && object.get("type").getAsString().equals("fortuna"))
            map = FORTUNA_ASCENSION_MAP;
        for(Item item : items)
            map.put(item, recipe) ;
    }

    public static AscensionRecipe getWeapon(Item item){
        return WEAPON_ASCENSION_MAP.get(item) ;
    }

    public static AscensionRecipe getFortuna(Item item){
        return FORTUNA_ASCENSION_MAP.get(item) ;
    }

    public static List<AscensionEntry> getWeaponAscensionList() {
        List<AscensionEntry> ret = new ArrayList<>() ;
        for(Map.Entry<Item, AscensionRecipe> entry : WEAPON_ASCENSION_MAP.entrySet()){
            for(int i = 0 ; i < 6 ; i ++) {
                if(entry.getValue().getRecipe(i) != null)
                    ret.add(new AscensionEntry(i, entry.getKey(), entry.getValue().getRecipe(i)));
            }
        }
        return ret ;
    }

    public static List<AscensionEntry> getFortunaAscensionList() {
        List<AscensionEntry> ret = new ArrayList<>() ;
        for(Map.Entry<Item, AscensionRecipe> entry : FORTUNA_ASCENSION_MAP.entrySet()){
            for(int i = 0 ; i < 6 ; i ++) {
                if(entry.getValue().getRecipe(i) != null)
                    ret.add(new AscensionEntry(i, entry.getKey(), entry.getValue().getRecipe(i)));
            }
        }
        return ret ;
    }

    public record AscensionEntry(int ascension, Item key, AscensionRecipe.Single recipe){}
}