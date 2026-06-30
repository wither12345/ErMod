
package net.mcreator.er.entity;

import org.checkerframework.checker.units.qual.s;

import net.wither.er.entity.goals.SyncTargetGoal;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;

import net.mcreator.er.init.ErModParticleTypes;
import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.EntityHurtEvent;

import javax.annotation.Nullable;

import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.util.Comparator;

public class ElectroCicinEntity extends Monster implements RangedAttackMob, OwnableEntity {
	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(ElectroCicinEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private int shotTimer = 0;

	public ElectroCicinEntity(EntityType<ElectroCicinEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(false);
		this.moveControl = new FlyingMoveControl(this, 10, true);
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new FlyingPathNavigation(this, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.9, 20) {
			@Override
			public boolean canContinueToUse() {
				return super.canContinueToUse() && ((ElectroCicinEntity) this.mob).shotTimer <= 0;
			}

			@Override
			protected Vec3 getPosition() {
				RandomSource random = ElectroCicinEntity.this.getRandom();
				double dir_x = ElectroCicinEntity.this.getX() + ((random.nextFloat() * 2 - 1) * 16);
				double dir_y = ElectroCicinEntity.this.getY() + ((random.nextFloat() * 2 - 1) * 16);
				double dir_z = ElectroCicinEntity.this.getZ() + ((random.nextFloat() * 2 - 1) * 16);
				return new Vec3(dir_x, dir_y, dir_z);
			}
		});
		this.goalSelector.addGoal(2, new RandomLookAroundGoal(this) {
			@Override
			public boolean canContinueToUse() {
				return super.canContinueToUse() && (ElectroCicinEntity.this).shotTimer <= 0;
			}
		});
		this.goalSelector.addGoal(0, new RangedAttackGoal(this, 1.25, 20, 10f) {
			@Override
			public boolean canContinueToUse() {
				return super.canContinueToUse() && (ElectroCicinEntity.this).shotTimer <= 0;
			}
		});
		this.targetSelector.addGoal(2, new SyncTargetGoal(this));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, Player.class, false, false) {
			@Override
			public boolean canUse() {
				return super.canUse() && !((ElectroCicinEntity.this).getOwner() instanceof Player);
			}
		});
		this.targetSelector.addGoal(7, new NearestAttackableTargetGoal(this, IronGolem.class, false, false) {
			@Override
			public boolean canUse() {
				return super.canUse() && !((ElectroCicinEntity.this).getOwner() instanceof Player);
			}
		});
		//		this.targetSelector.addGoal(1, new ErOwnerHurtTargetGoal(this));
	}

	public void setTarget(@Nullable LivingEntity entity) {
		if (EntityHurtEvent.shouldHurt(this, entity))
			super.setTarget(entity);
	}

	@Override
	public void tick() {
		super.tick();
		if (shotTimer > 0) {
			shotTimer--;
			//this.setNoAi(true);
			if (shotTimer == 20)
				this.setDeltaMovement(this.getViewVector(1f));
			//else if (shotTimer == 0)
			//	this.setNoAi(false);
			final Vec3 _center = new Vec3(this.getX(), this.getY(), this.getZ());
			List<LivingEntity> _entfound = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(1), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
			for (LivingEntity living : _entfound) {
				if (living != this && !this.isOwnedBy(living)) {
					if (!(living instanceof OwnableEntity && this.isOwnedBy(((OwnableEntity) living).getOwner()))) {
						this.doHurtTarget(living);
						shotTimer = 0;
					}
				}
			}
			if (this.level() instanceof ServerLevel server)
				server.sendParticles((SimpleParticleType) (ErModParticleTypes.SMALL_ELECTRO_PARTICLE.get()), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
		}
		if (this.getOwner() instanceof Player)
			this.setHealth(this.getHealth() - this.getMaxHealth() * 0.005f);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.death"));
	}

	@Override
	public LivingEntity getOwner() {
		UUID uuid = this.getOwnerUUID();
		if (this.level() instanceof ServerLevel) {
			Entity entity = uuid == null ? null : ((ServerLevel) this.level()).getEntity(uuid);
			return entity instanceof LivingEntity livi ? livi : null;
		}
		return null;
	}

	@Override
	public boolean causeFallDamage(float l, float d, DamageSource source) {
		return false;
	}

	@Override
	public boolean hurt(DamageSource damagesource, float amount) {
		if (damagesource.is(DamageTypes.LIGHTNING_BOLT))
			return false;
		return super.hurt(damagesource, amount);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float flval) {
		ElementalProjectileEntity.shoot(this.level(),this,RandomSource.create(),2,this.getAttributeValue(Attributes.ATTACK_DAMAGE) / 3,0);
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public void setNoGravity(boolean ignored) {
		super.setNoGravity(true);
	}

	@Nullable
	@Override
	public UUID getOwnerUUID() {
		return this.entityData.get(DATA_OWNERUUID_ID).orElse(null);
	}

	public void setOwnerUUID(@Nullable UUID p_21817_) {
		this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(p_21817_));
	}

	public boolean isOwnedBy(LivingEntity p_21831_) {
		return p_21831_ == this.getOwner();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder p_326034_) {
		super.defineSynchedData(p_326034_);
		p_326034_.define(DATA_OWNERUUID_ID, Optional.empty());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag p_21819_) {
		super.addAdditionalSaveData(p_21819_);
		if (this.getOwnerUUID() != null) {
			p_21819_.putUUID("Owner", this.getOwnerUUID());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag p_21815_) {
		super.readAdditionalSaveData(p_21815_);
		UUID uuid;
		if (p_21815_.hasUUID("Owner")) {
			uuid = p_21815_.getUUID("Owner");
		} else {
			String s = p_21815_.getString("Owner");
			uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
		}
		if (uuid != null) {
			try {
				this.setOwnerUUID(uuid);
			} catch (Throwable throwable) {
			}
		}
	}

	public void shot(LivingEntity entity, int timer) {
		shotTimer = timer;
		this.lookAt(entity, 30.0F, 30.0F);
	}

	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
		event.register(ErModEntities.ELECTRO_CICIN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)),
				RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.5);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(Attributes.FLYING_SPEED, 0.5);
		return builder;
	}
}
