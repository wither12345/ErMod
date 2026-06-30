package net.wither.er.recipe.converting;

import com.google.gson.*;
import net.mcreator.er.ErMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Mod.EventBusSubscriber
public class AlchemyConvertingRecipeListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).create();
    private static Map<ResourceLocation, JsonElement> mapToRead ;
    private static final Map<ResourceLocation, AlchemyConvertingRecipe> recipes = new HashMap<>();

    public AlchemyConvertingRecipeListener() {
        super(GSON, "converting");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        mapToRead = map;
    }

    public static List<AlchemyConvertingRecipe> getRecipes(Item item){
        List<AlchemyConvertingRecipe> recipeToReturn = new ArrayList<>();
        for(AlchemyConvertingRecipe recipe : recipes.values()){
            if(recipe.has(item))
                recipeToReturn.add(recipe) ;
        }
        return recipeToReturn;
    }

    private static void read(ResourceLocation location, JsonObject object, RegistryAccess access){
        Item mainMaterial = BuiltInRegistries.ITEM.get(new ResourceLocation(object.get("item").getAsString()));
        int count = object.get("count").getAsInt();
        JsonArray itemsArray = object.getAsJsonArray("convertibles") ;
        Set<Item> items = new HashSet<>(itemsArray.size()) ;
        ArrayList<ItemStack> results = new ArrayList<>(itemsArray.size()) ;
        for(JsonElement itemElement : itemsArray){
            if(itemElement.isJsonObject()){
                try {
                    CompoundTag tag = TagParser.parseTag(itemElement.toString());
                    ItemStack itemStack = ItemStack.of(tag);
                    results.add(itemStack);
                    items.add(itemStack.getItem());
                } catch (Exception e) {
                    ErMod.LOGGER.error(e);
                }
            }
            else {
                String s = itemElement.getAsString();
                if (s.charAt(0) != '#') {
                    Item itemGet = BuiltInRegistries.ITEM.get(new ResourceLocation(s));
                    if (itemGet != Items.AIR) {
                        putItem(items, results, itemGet);
                    }
                } else {
                    s = s.substring(1);
                    ResourceLocation tagId = new ResourceLocation(s);
                    TagKey<Item> tagKey = ItemTags.create(tagId);
                    HolderSet.Named<Item> holders = BuiltInRegistries.ITEM.getOrCreateTag(tagKey);
                    for(Holder<Item> itemHolder : holders){
                        items.add(itemHolder.value());
                        results.add(new ItemStack(itemHolder));
                    }
                }
            }
        }
        if(recipes.containsKey(location))
            recipes.get(location).addItem(items);
        else
            recipes.put(location, new AlchemyConvertingRecipe(mainMaterial, count, items, results)) ;
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        mapToRead.forEach((location, element) -> {
            try {
                read(location, element.getAsJsonObject(), event.getServer().registryAccess());
            } catch (Exception var6) {
                ErMod.LOGGER.error("Parsing error loading custom converting {}: {}", location, var6.getMessage());
            }
        });
        mapToRead.clear();
        mapToRead = null;
    }

    private static void putItem(Set<Item> items, ArrayList<ItemStack> results, Item item){
        items.add(item);
        results.add(new ItemStack(item));
    }

    public static List<AlchemyConvertingRecipe> getRecipeList(){
        return recipes.values().stream().toList();
    }
}
