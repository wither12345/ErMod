package net.wither.er.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BuffOrbEntity extends Entity {
    private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(BuffOrbEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<BlockPos>> BLOCK_POS_DATA = SynchedEntityData.defineId(BuffOrbEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    @Nullable
    private UUID targetUUID;
    @Nullable
    private Entity cachedTarget;

    public BuffOrbEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void tick() {
        Entity target = this.getTarget();
        if (target == null) {
            if (this.getSavePos() != null)
                this.setPos(this.getSavePos().getCenter());
            if (this.level() instanceof ServerLevel)
                this.setTarget(this.level().getNearestPlayer(this, 5));
        } else {
            Vec3 vec3 = target.position().subtract(this.position());
            double d = vec3.lengthSqr();
            if (d > 25 && !this.level().isClientSide())
                this.setTarget(null);
            else if (d > 0.25)
                this.move(MoverType.SELF, vec3.normalize().scale(0.2));
            else {
                this.onTouch(target);
                this.discard();
            }
        }
    }

    public void setSavePos(@Nullable BlockPos savePos) {
        this.entityData.set(BLOCK_POS_DATA, savePos == null ? Optional.empty() : Optional.of(savePos));
    }

    @Nullable
    public BlockPos getSavePos() {
        return this.entityData.get(BLOCK_POS_DATA).orElse(null);
    }

    abstract protected void onTouch(Entity entity);

    public void setTarget(@Nullable Entity target) {
        this.entityData.set(TARGET, target == null ? 0 : target.getId() + 1);
        if (target != null) {
            this.targetUUID = target.getUUID();
            this.cachedTarget = target;
        }
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> dataAccessor) {
        if (TARGET.equals(dataAccessor)) {
            int i = this.getEntityData().get(TARGET);
            if (i > 0) {
                this.cachedTarget = this.level().getEntity(i - 1);
                if (this.cachedTarget != null)
                    this.targetUUID = this.cachedTarget.getUUID();
            } else {
                this.cachedTarget = null;
                this.targetUUID = null;
            }
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    @Nullable
    public Entity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        } else {
            if (this.targetUUID != null) {
                Level var2 = this.level();
                if (var2 instanceof ServerLevel serverlevel) {
                    this.cachedTarget = serverlevel.getEntity(this.targetUUID);
                    return this.cachedTarget;
                }
            }
            return null;
        }
    }

    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        if (this.targetUUID != null)
            tag.putUUID("Target", this.targetUUID);
        BlockPos pos = this.getSavePos();
        if (pos != null)
            tag.putIntArray("SavePos", List.of(pos.getX(), pos.getY(), pos.getZ()));
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Target")) {
            this.targetUUID = tag.getUUID("Target");
            this.cachedTarget = null;
        }
        if (tag.contains("SavePos", 11)) {
            int[] integers = tag.getIntArray("SavePos");
            if (integers.length > 2)
                this.setSavePos(new BlockPos(integers[0], integers[1], integers[2]));
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(TARGET, 0);
        builder.define(BLOCK_POS_DATA, Optional.empty());
    }
}
