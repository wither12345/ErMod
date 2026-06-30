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
				/*
				LevelAccessor world = this.level();
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				double elemental_mastery = 0;
				int level = 0;
				if (this.sourceEntity != null) {
					elemental_mastery = this.sourceEntity.getAttribute(ErModAttributes.ELEMENTAL_MASTERY.get()).getValue();
					level = EntityHurtEvent.getEntityLevel(this.sourceEntity);
				}
				entity.hurt(new ErDamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:reaction"))), this.sourceEntity, 4, 0),
						(float) (2 * EntityHurtEvent.getElementalMasteryMultiply(1, elemental_mastery) * EntityHurtEvent.getLevelMultiply(level)));
				entity.setDeltaMovement(new Vec3(0, 0, 0));
				{
					final Vec3 _center = new Vec3(x, y, z);
					List<LivingEntity> _entfound = world.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
					for (LivingEntity entityiterator : _entfound) {
						if ((this.sourceEntity == null || !(entityiterator == this.sourceEntity || (entityiterator instanceof TamableAnimal _tamEnt ? (Entity) _tamEnt.getOwner() : null) == this.sourceEntity)) && entityiterator instanceof LivingEntity
								&& entityiterator.getPersistentData().getInt("Hydro") > 0 && entityiterator.getPersistentData().getInt("Electro_Charged_Cd") <= 0) {
							entityiterator.getPersistentData().putInt("Electro_Charged_Cd", 10);
							if (world instanceof ServerLevel _level) {
								ArcEntity entityToSpawn = ErModEntities.ARC.get().spawn(_level, BlockPos.containing(x, y + entity.getBbHeight() * 0.7, z), MobSpawnType.MOB_SUMMONED);
								entityToSpawn.setActiveTarget(entityiterator.getId());
								entityToSpawn.setSource(this.sourceEntity);
								if (entityToSpawn != null) {
									entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
								}
							}
						}
					}
				}
				entity.getPersistentData().putInt("Hydro", entity.getPersistentData().getInt("Hydro") - 10);
				*/
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