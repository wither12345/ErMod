package net.wither.er.outcrop;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.mcreator.er.ErMod;
import net.mcreator.er.procedures.ApplyErlevelProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.init.AdditionalRegistries;
import net.wither.er.network.SyncLevelData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class OutcropWave {
    private Collection<EntityWithModifier> pools ;
    public int quality ;

    public Collection<EntityWithModifier> getPools() {
        return pools;
    }

    public static OutcropWave read(JsonElement element){
        OutcropWave ret = new OutcropWave();
        ret.pools = new ArrayList<>() ;
        try {
            ret.quality = element.getAsJsonObject().get("quality").getAsInt();
            JsonArray pool = element.getAsJsonObject().get("pools").getAsJsonArray() ;
            for(JsonElement enti : pool) {
                String type = enti.getAsJsonObject().get("type").getAsString() ;
                int count = enti.getAsJsonObject().get("count").getAsInt();
                EntityWithModifier entity = new EntityWithModifier(BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(type)), count, new ArrayList<EntityModifier>());
                if(enti.getAsJsonObject().get("modifiers") != null) {
                    JsonObject modifiers = enti.getAsJsonObject().get("modifiers").getAsJsonObject();
                    Set<String> keys = modifiers.keySet();
                    for (String key : keys) {
                        EntityModifier modifier = AdditionalRegistries.ENTITY_MODIFIER_REGISTRY.get(ResourceLocation.parse(key));
                        if (modifier == null) {
                            continue;
                        }
                        modifier = modifier.copy();
                        modifier.read(modifiers.get(key));
                        entity.addModifier(modifier);
                    }
                }
                ret.pools.add(entity);
            }
        }
        catch(Exception var6) {
            ErMod.LOGGER.error(var6);
        }
        return ret ;
    }

    public record EntityWithModifier(EntityType<?> type, int count, ArrayList<EntityModifier> modifiers) {
        public void addModifier(EntityModifier modifier) {
                modifiers.add(modifier);
            }

            public void spawn(ServerLevel level, int x, int y, int z, int range, int entity_level, Blossom owner) {
                int cnt = 0;
                for (int i = 0; i < this.count; i++) {
                    int find_x = x + Mth.nextInt(RandomSource.create(), -range, range);
                    int find_z = z + Mth.nextInt(RandomSource.create(), -range, range);
                    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(find_x, y - 2, find_z);
                    while (pos.getY() > level.getMinBuildHeight() && level.getBlockState(pos).canOcclude()) {
                        pos.move(Direction.UP);
                    }
                    Entity entityToSpawn = type.spawn(level, pos, MobSpawnType.MOB_SUMMONED);
                    if (entityToSpawn != null) {
                        cnt++;
                        entityToSpawn.setYRot(level.getRandom().nextFloat() * 360F);
                        for (EntityModifier modifier : modifiers) {
                            modifier.apply(entityToSpawn, entity_level);
                        }
                        ApplyErlevelProcedure.execute(entityToSpawn, entity_level);
                        PacketDistributor.sendToAllPlayers(new SyncLevelData(entityToSpawn.getId(), entityToSpawn.getPersistentData().getInt("erLevel")));
                        entityToSpawn.getPersistentData().putUUID("BlossomOwner", owner.getUUID());
                    }
                }
                owner.addMobLeft(cnt);
            }
        }
}
