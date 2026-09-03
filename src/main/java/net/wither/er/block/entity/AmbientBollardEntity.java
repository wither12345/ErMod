package net.wither.er.block.entity;

import net.mcreator.er.init.ErModBlockEntities;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.wither.er.entity.BuffOrbEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class AmbientBollardEntity extends BlockEntity implements TraceableEntity {
    private int ticker;
    @Nullable private UUID ownerUUID;
    @Nullable private Entity cachedOwner;
    @NotNull private EntityType<?> spawnType = ErModEntities.HEAL_ORB.get();
    private int interactCd = 0;

    public AmbientBollardEntity(BlockPos blockPos, BlockState blockState) {
        super(ErModBlockEntities.AMBIENT_BOLLARD.get(), blockPos, blockState);
    }

    public void setSpawn(@NotNull EntityType<?> spawnType) {
        this.spawnType = spawnType;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if(blockEntity instanceof AmbientBollardEntity entity &&
                (entity.getOwner() == null || entity.getOwner().isRemoved()) &&
                level instanceof ServerLevel serverLevel){
            entity.ticker ++ ;
            if(entity.interactCd > 0)
                entity.interactCd --;
            if(entity.ticker >= 1200){
                entity.ticker = 0;
                BlockPos posAbv = blockPos.above();
                Entity entityToSpawn = entity.spawnType.spawn(serverLevel, posAbv, MobSpawnType.SPAWNER);
                if(entityToSpawn instanceof BuffOrbEntity buffOrb)
                    buffOrb.setSavePos(posAbv);
                entity.setOwner(entityToSpawn);
                entity.setChanged();
            }
        }
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
            this.cachedOwner = null;
        }
        if(tag.contains("SpawnType")) {
            this.spawnType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(tag.getString("SpawnType")));
        }
        this.ticker = tag.getInt("Ticker");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(tag, provider);
        if (this.ownerUUID != null)
            tag.putUUID("Owner", this.ownerUUID);
        tag.putString("SpawnType", BuiltInRegistries.ENTITY_TYPE.getKey(this.spawnType).toString());
        tag.putInt("Ticker", this.ticker);
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

    public Component trySwitch(){
        if(this.interactCd > 0) return Component.empty();
        this.interactCd = 0;
        if(this.spawnType == ErModEntities.HEAL_ORB.get()) {
            this.spawnType = ErModEntities.SPEED_ORB.get();
            return Component.translatable("lore.er.ambient.speed");
        }
        else if(this.spawnType == ErModEntities.SPEED_ORB.get()) {
            this.spawnType = ErModEntities.HEAL_ORB.get();
            return Component.translatable("lore.er.ambient.heal");
        }
        return Component.empty();
    }
}
