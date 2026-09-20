package net.wither.er.block.entity;

import net.mcreator.er.init.ErModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class BossSpawnerEntity extends BlockEntity implements TraceableEntity {
    @Nullable private UUID ownerUUID;
    @Nullable private Entity cachedOwner;
    @Nullable private EntityType<?> spawnEntity = null;
    private int waitingTime;
    public BossSpawnerEntity(BlockPos blockPos, BlockState blockState) {
        super(ErModBlockEntities.BOSS_SPAWNER.get(), blockPos, blockState);
    }

    public static void ticking(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if(entity instanceof BossSpawnerEntity bossSpawner && level instanceof ServerLevel serverLevel){
            if(bossSpawner.getOwner() == null && bossSpawner.waitingTime ++ > 400){
                bossSpawner.waitingTime = 0;
                bossSpawner.spawn(serverLevel, pos);
            }
        }
    }

    public void spawn(ServerLevel level, BlockPos pos){
        if (this.spawnEntity != null) {
            this.setOwner(this.spawnEntity.spawn(level, pos.above(), MobSpawnType.SPAWNER));
        }
    }

    public void setSpawn(EntityType<?> type){
        this.spawnEntity = type;
        this.setOwner(null);
        this.waitingTime = 0;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
            this.cachedOwner = null;
        }
        if(tag.contains("Spawn")){
            this.spawnEntity = BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(tag.getString("Spawn")));
        }
        this.waitingTime = tag.getInt("WaitingTime");
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.ownerUUID != null) tag.putUUID("Owner", this.ownerUUID);
        if (this.spawnEntity != null) tag.putString("Spawn", BuiltInRegistries.ENTITY_TYPE.getKey(this.spawnEntity).toString());
        tag.putInt("WaitingTime", this.waitingTime);
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUUID = entity.getUUID();
            this.cachedOwner = entity;
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else {
            if (this.ownerUUID != null) {
                Level var2 = this.getLevel();
                if (var2 instanceof ServerLevel serverlevel) {
                    this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
                    return this.cachedOwner;
                }
            }

            return null;
        }
    }
}
