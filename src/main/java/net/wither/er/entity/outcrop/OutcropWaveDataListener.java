package net.wither.er.entity.outcrop;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.mcreator.er.ErMod;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class OutcropWaveDataListener extends SimpleJsonResourceReloadListener{
    private static final Gson GSON = (new GsonBuilder()).create();
    private static final ArrayList<OutcropWave> waves = new ArrayList<OutcropWave>();
    private static final Map<ResourceLocation, OutcropWave> waveMap = new HashMap<>();

    public OutcropWaveDataListener() {
        super(GSON, "outcrop_wave");
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager manager, @NotNull ProfilerFiller filler) {
        RegistryOps<JsonElement> registryops = this.makeConditionalOps();
        ImmutableMap.Builder<ResourceLocation, AdvancementHolder> builder = ImmutableMap.builder();
        map.forEach((location, element) -> {
            try {
                OutcropWave wave = OutcropWave.read(element, location) ;
                waves.add(wave) ;
                waveMap.put(location, wave);
            } catch (Exception var6) {
                ErMod.LOGGER.error("Parsing error loading custom outcrop wave {}: {}", location, var6.getMessage());
            }
        });
        waves.sort(Comparator.comparingInt(wave -> wave.quality));
    }

    public static ArrayList<OutcropWave> getAllWaves() {
        return waves;
    }

    public static OutcropWave getByLocation(ResourceLocation location){
        return waveMap.get(location);
    }
}