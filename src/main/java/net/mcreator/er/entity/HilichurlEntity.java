
package net.mcreator.er.entity;


import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.procedures.HilichurlOnInitialEntitySpawnProcedure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class HilichurlEntity extends Monster implements CrossbowAttackMob, InventoryCarrier {
	private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(HilichurlEntity.class, EntityDataSerializers.BOOLEAN);
	private static final int INVENTORY_SIZE = 5;
	private static final int SLOT_OFFSET = 300;
	private final SimpleContainer inventory = new SimpleContainer(5);

	public HilichurlEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ErModEntities.HILICHURL.get(), world);
	}

	public HilichurlEntity(EntityType<HilichurlEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(false);
	}

	public boolean isChargingCrossbow() {
		return this.entityData.get(IS_CHARGING_CROSSBOW);
	}

	@Override
	public void onCrossbowAttackPerformed() {
		this.noActionTime = 0;
	}

	@Override
	public void setChargingCrossbow(boolean p_33302_) {
		this.entityData.set(IS_CHARGING_CROSSBOW, p_33302_);
	}

	@Override
	public void shootCrossbowProjectile(@NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, @NotNull Projectile projectile, float v) {
		this.shootCrossbowProjectile(this, livingEntity, projectile, v, 1.6F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(IS_CHARGING_CROSSBOW, false);
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		this.writeInventoryToTag(compoundTag);
	}

	@Override
	public @NotNull SimpleContainer getInventory() {
		return this.inventory;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new RangedBowAttackGoal(this, 0D, 25, 15.0F) {
			@Override
			public boolean canUse() {
				LivingEntity entity = HilichurlEntity.this;
				return entity.getMainHandItem().getItem() instanceof BowItem;
			}

			@Override
			public boolean canContinueToUse() {
				LivingEntity entity = HilichurlEntity.this;
				return entity.getMainHandItem().getItem() instanceof BowItem;
			}
		});
		this.goalSelector.addGoal(1, new RangedCrossbowAttackGoal(this, 1.0D, 8.0F) {
			@Override
			public boolean canUse() {
				LivingEntity entity = HilichurlEntity.this;
				return entity.getMainHandItem().getItem() instanceof CrossbowItem;
			}

			@Override
			public boolean canContinueToUse() {
				LivingEntity entity = HilichurlEntity.this;
				return entity.getMainHandItem().getItem() instanceof CrossbowItem;
			}
		});
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
			@Override
			public boolean canUse() {
				LivingEntity entity = HilichurlEntity.this;
				return !(entity.getMainHandItem().getItem() instanceof CrossbowItem && entity.getMainHandItem().getItem() instanceof BowItem);
			}

			@Override
			public boolean canContinueToUse() {
				LivingEntity entity = HilichurlEntity.this;
				return !(entity.getMainHandItem().getItem() instanceof CrossbowItem && entity.getMainHandItem().getItem() instanceof BowItem);
			}
		});
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true, false));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true, false));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, true, false));
		this.goalSelector.addGoal(5, new BreakDoorGoal(this, e -> true));
		this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1));
		this.targetSelector.addGoal(7, new HurtByTargetGoal(this).setAlertOthers());
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(9, new FloatGoal(this));
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.generic.death"));
	}

	@Override
	public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData livingData, CompoundTag tag) {
		SpawnGroupData retrieval = super.finalizeSpawn(world, difficulty, reason, livingData, tag);
		HilichurlOnInitialEntitySpawnProcedure.execute(this);
		return retrieval;
	}

	@Override
	public void performRangedAttack(LivingEntity target, float flval) {
		Arrow entityarrow = new Arrow(this.level(), this);
		double d0 = target.getY() + target.getEyeHeight() - 1.1;
		double d1 = target.getX() - this.getX();
		double d3 = target.getZ() - this.getZ();
		entityarrow.shoot(d1, d0 - entityarrow.getY() + Math.sqrt(d1 * d1 + d3 * d3) * 0.2F, d3, 1.6F, 12.0F);
		this.level().addFreshEntity(entityarrow);
	}

	public static void init() {
		SpawnPlacements.register(ErModEntities.HILICHURL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 20);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}
}
