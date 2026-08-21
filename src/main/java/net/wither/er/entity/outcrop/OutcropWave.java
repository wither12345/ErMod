package net.wither.er.entity.outcrop;

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
import net.minecraftforge.network.PacketDistributor;
import net.wither.er.init.AdditionalRegistries;
import net.wither.er.network.SyncLevelData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class OutcropWave {
    private Collection<EntityWithModifier> pools ;
    public int quality ;
    private final ResourceLocation location;

    public OutcropWave(ResourceLocation location){
        this.location = location;
    }

    public Collection<EntityWithModifier> getPools() {
        return pools;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public static OutcropWave read(JsonElement element, ResourceLocation location){
        OutcropWave ret = new OutcropWave(location);
        ret.pools = new ArrayList<>() ;
        try {
            ret.quality = element.getAsJsonObject().get("quality").getAsInt();
            JsonArray pool = element.getAsJsonObject().get("pools").getAsJsonArray() ;
            for(JsonElement entity_element : pool) {
                String type = entity_element.getAsJsonObject().get("type").getAsString() ;
                int count = entity_element.getAsJsonObject().get("count").getAsInt();
                EntityWithModifier entity = new EntityWithModifier(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(type)), count, new ArrayList<EntityModifier>());
                JsonElement modifierElement = entity_element.getAsJsonObject().get("modifiers");
                if(modifierElement != null) {
                    if(modifierElement.isJsonArray()){
                        for(JsonElement ele : modifierElement.getAsJsonArray()){
                            String modifier_type = ele.getAsJsonObject().get("type").getAsString();
                            EntityModifier.Builder builder = AdditionalRegistries.ENTITY_MODIFIERS_REGISTRY.getValue(new ResourceLocation(modifier_type));
                            if(builder == null)
                                continue;;
                            EntityModifier modifier = builder.build(ele);
                            entity.addModifier(modifier);
                        }
                    }
                    else {
                        JsonObject modifiers = modifierElement.getAsJsonObject();
                        Set<String> keys = modifiers.keySet();
                        for (String key : keys) {
                            EntityModifier.Builder builder = AdditionalRegistries.ENTITY_MODIFIERS_REGISTRY.getValue(new ResourceLocation(key));
                            if (builder == null)
                                continue;
                            EntityModifier modifier = builder.build(modifiers.get(key));
                            entity.addModifier(modifier);
                        }
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
                    ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SyncLevelData(entityToSpawn.getId(), entityToSpawn.getPersistentData().getInt("erLevel")));
                    entityToSpawn.getPersistentData().putUUID("BlossomOwner", owner.getUUID());
                }
            }
            owner.addMobLeft(cnt);
        }
        }
}
