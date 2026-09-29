package net.wither.er.item.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber
public class ErFoodDataListener extends SimpleJsonResourceReloadListener {
    private static final Map<Item, FoodDataProvider> FOOD_MAP = new HashMap<>();
    private static Map<ResourceLocation, JsonElement> cathedMap;
    private static final Gson GSON = (new GsonBuilder()).create();
    public ErFoodDataListener() {
        super(GSON, "er_food");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> elementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        FOOD_MAP.clear();
        cathedMap = elementMap;
    }

    @SubscribeEvent
    public static void onEvent(ServerStartedEvent event){
        cathedMap.forEach((location, element) ->{
            FoodDataProvider provider;
            JsonObject object = element.getAsJsonObject();
            if(object.has("food_addition"))
                provider = ErFoodData.CODEC.parse(JsonOps.INSTANCE, object.get("food_addition")).result().orElse(null);
            else if(object.has("food_additions") && object.get("food_additions").isJsonArray()){
                ErFoodDataGroup.Builder dataMap = new ErFoodDataGroup.Builder();
                object.get("food_additions").getAsJsonArray().forEach(addition -> {
                            JsonObject obj = addition.getAsJsonObject();
                            if(obj.has("components") && obj.has("addition")){
                                Optional<CompoundTag> map = CompoundTag.CODEC.parse(JsonOps.INSTANCE, obj.get("components")).result();
                                if(map.isEmpty()) {
                                    ErFoodData.CODEC.parse(JsonOps.INSTANCE, obj.get("addition")).result().ifPresent(dataMap::setDefault);
                                    return;
                                }
                                ErFoodData.CODEC.parse(JsonOps.INSTANCE, obj.get("addition")).result().ifPresent(
                                        data -> dataMap.put(new ErFoodDataGroup.Simple(map.get(), data)));
                            }
                        }
                );
                provider = dataMap.build();
            } else {
                return;
            }
            if(object.has("items")) {
                for(JsonElement item_element : object.get("items").getAsJsonArray()){
                    String id = item_element.getAsString();
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
                    if (item != Items.AIR)
                        FOOD_MAP.put(item, provider);
                }
            }
            else if(object.has("item")) {
                String id = object.get("item").getAsString();
                if (id.charAt(0) == '#') {
                    TagKey<Item> tag = TagKey.create(Registries.ITEM, new ResourceLocation(id.substring(1)));
                    ITagManager<Item> manager = ForgeRegistries.ITEMS.tags();
                    if (manager != null) {
                        manager.getTag(tag).size();
                        manager.getTag(tag).forEach(item -> FOOD_MAP.put(item, provider));
                    }
                } else {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
                    if (item != Items.AIR)
                        FOOD_MAP.put(item, provider);
                }
            }
        });
        cathedMap.clear();
        cathedMap = null;
    }

    @Nullable
    public static ErFoodData getData(ItemStack item){
        if(FOOD_MAP.containsKey(item.getItem()))
            return FOOD_MAP.get(item.getItem()).getFoodData(item);
        return null;
    }
}
