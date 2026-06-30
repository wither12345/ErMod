package net.wither.er.outcrop;

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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class OutcropWaveDataListener extends SimpleJsonResourceReloadListener{
    private static final Gson GSON = (new GsonBuilder()).create();
    private static final ArrayList<OutcropWave> waves = new ArrayList<OutcropWave>();

    public OutcropWaveDataListener() {
        super(GSON, "outcrop_wave");
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller filler) {
        RegistryOps<JsonElement> registryops = this.makeConditionalOps();
        ImmutableMap.Builder<ResourceLocation, AdvancementHolder> builder = ImmutableMap.builder();
        map.forEach((location, element) -> {
            try {
                OutcropWave wave = OutcropWave.read(element) ;
                waves.add(wave) ;
            } catch (Exception var6) {
                ErMod.LOGGER.error("Parsing error loading custom outcrop wave {}: {}", location, var6.getMessage());
            }
        });
        waves.sort(Comparator.comparingInt(wave -> wave.quality));
    }

    public static ArrayList<OutcropWave> getAllWaves() {
        return waves;
    }
}