package net.wither.er.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.wither.er.elements.Electro;

import javax.annotation.Nullable;

public class ArcEntity extends Entity {
	private static final EntityDataAccessor<Integer> DATA_ID_TARGET = SynchedEntityData.defineId(ArcEntity.class, EntityDataSerializers.INT);
	private LivingEntity clientSideCachedTarget;
	private float restTime;
	private LivingEntity sourceEntity;
	private boolean onlyEffect = false;

	public ArcEntity(EntityType<ArcEntity> type, Level world) {
		super(type, world);
	}

	@Nullable
	public LivingEntity getTarget() {
		if (!this.hasActiveTarget()) {
			return null;
		} else if (this.level().isClientSide) {
			if (this.clientSideCachedTarget != null) {
				return this.clientSideCachedTarget;
			} else {
				Entity entity = this.level().getEntity(this.entityData.get(DATA_ID_TARGET));
				if (entity instanceof LivingEntity) {
					this.clientSideCachedTarget = (LivingEntity) entity;
					return this.clientSideCachedTarget;
				} else {
					return null;
				}
			}
		} else {
			Entity entity = this.level().getEntity(this.entityData.get(DATA_ID_TARGET));
			if (entity instanceof LivingEntity) {
				return (LivingEntity) entity;
			} else {
				return null;
			}
		}
	}

	public boolean hasActiveTarget() {
		return this.entityData.get(DATA_ID_TARGET) != 0;
	}

	public void setActiveTarget(int p_32818_) {
		this.entityData.set(DATA_ID_TARGET, p_32818_);
	}

	public void setSource(LivingEntity entity) {
		this.sourceEntity = entity;
	}

	public void setOnlyEffect(boolean only) {
		this.onlyEffect = only;
	}

	public float getRestScale(float p_32813_) {
		return 1f - (restTime / 10f);
	}

	public float getRest() {
		return restTime;
	}

	@Override
	public void tick() {
		super.tick();
		if (restTime == 1) {
			LivingEntity entity = this.getTarget();
			if (entity != null && entity.isAlive()) {
				if (onlyEffect) {
					restTime += 1f;
					return;
				}
                Electro.doElectroCharged(entity,sourceEntity);
            }
		}
		if (restTime >= 5)
			this.kill();
		restTime += 1f;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_ID_TARGET, 0);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> p_32834_) {
		super.onSyncedDataUpdated(p_32834_);
		if (DATA_ID_TARGET.equals(p_32834_)) {
			this.clientSideCachedTarget = null;
		}
	}

	public static void init() {
	}
}