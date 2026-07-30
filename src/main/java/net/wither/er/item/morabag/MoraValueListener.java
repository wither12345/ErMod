package net.wither.er.item.morabag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.mcreator.er.ErMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoraValueListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).create();
    public static List<MoraBagItemPlus.MoraVal> moraVals = new ArrayList<>();

    public MoraValueListener() {
        super(GSON, "mora_value");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        for(JsonElement element : resourceLocationJsonElementMap.values()){
            try {
                read(element);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void read(JsonElement element){
        JsonObject object = element.getAsJsonObject();
        ResourceLocation location = new ResourceLocation(object.get("item").getAsString());
        Item item = BuiltInRegistries.ITEM.get(location);
        if(item != Items.AIR){
            int val = object.get("value").getAsInt();
            moraVals.add(new MoraBagItemPlus.MoraVal(item, val));
        }
        else ErMod.LOGGER.info("Skip for not find item: {}", location);
    }
}
