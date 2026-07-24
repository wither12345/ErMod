package net.mcreator.er.entity;

import net.wither.er.shield.ShieldStack;
import net.wither.er.init.ShieldRegistry;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.entity.CicinMage;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.er.init.ErModParticleTypes;
import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModEntities;

import java.util.List;
import java.util.Comparator;

public class FatuiElectroCicinMageEntity extends CicinMage {
	public FatuiElectroCicinMageEntity(EntityType<FatuiElectroCicinMageEntity> type, Level world) {
		super(type, world);
		xpReward = 15;
		setNoAi(false);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ErModItems.ELECTRO_MIST_GRASS_LANTERN.get()));
	}

	private int shieldCd = 800;

	@Override
	public void tick() {
		super.tick();
		if (shieldCd > 0)
			shieldCd--;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true, false));
		this.goalSelector.addGoal(0, new MistyCallGoal(this, ErModEntities.ELECTRO_CICIN.get(), 3));
		this.goalSelector.addGoal(0, new ThunderShieldGoal(this));
		//this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 2, true));
		this.goalSelector.addGoal(0, new KeepDistanceGoal(this, 0.5, 10));
		this.goalSelector.addGoal(1, new HurtlingBoltsGoal(this));
		this.goalSelector.addGoal(1, new ThunderboltGoal(this));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(5, new FloatGoal(this));
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.death"));
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
		event.register(ErModEntities.FATUI_ELECTRO_CICIN_MAGE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)),
				RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 45);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		return builder;
	}

	static class HurtlingBoltsGoal extends Goal {
		private final CicinMage mage;
		private int cd = 0;

		public HurtlingBoltsGoal(FatuiElectroCicinMageEntity mage) {
			this.mage = mage;
		}

		@Override
		public boolean canUse() {
			mage.transferCash();
            mage.summonings.removeIf(sum -> sum == null || !sum.isAlive());
			return mage.getTarget() != null && mage.globalCd <= 0 && mage.summonings.size() > 0;
		}

		@Override
		public boolean canContinueToUse() {
			return cd-- > 0;
		}

		@Override
		public void start() {
			cd = 100;
			int i = 0;
			LivingEntity target = mage.getTarget();
			if (target != null && target.isAlive())
				for (LivingEntity sum : mage.summonings) {
					i++;
					BlockPos pos = mage.level().clip(new ClipContext(mage.getEyePosition(1f), mage.getEyePosition(1f).add(mage.getViewVector(1f)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, mage)).getBlockPos();
					Vec3 viewVec = mage.getViewVector(1f);
					sum.teleportTo(pos.getX() + viewVec.x * Mth.nextDouble(RandomSource.create(), 1, 2), pos.getY(), pos.getZ() + viewVec.z * Mth.nextDouble(RandomSource.create(), 1, 2));
					if (sum instanceof ElectroCicinEntity cicin)
						cicin.shot(target, 20 + i * 10);
				}
		}

		@Override
		public void tick() {
		}

		@Override
		public void stop() {
		}
	}

	static class ThunderboltGoal extends Goal {
		private final CicinMage mage;
		private int cd = 0;
		private double x;
		private double y;
		private double z;

		public ThunderboltGoal(FatuiElectroCicinMageEntity mage) {
			this.mage = mage;
		}

		@Override
		public boolean canUse() {
			return mage.getTarget() != null && mage.globalCd <= 0;
		}

		@Override
		public boolean canContinueToUse() {
			return cd-- > 0;
		}

		@Override
		public void start() {
			cd = 120;
			int i = 0;
			mage.globalCd = 25;
			LivingEntity target = mage.getTarget();
			if (target != null && target.isAlive()) {
				x = target.getX();
				y = target.getY();
				z = target.getZ();
			}
		}

		@Override
		public void tick() {
			if (cd > 100) {
				if (this.mage.level() instanceof ServerLevel server)
					server.sendParticles(ErModParticleTypes.SMALL_ELECTRO_PARTICLE.get(), x, y, z, 5, 1, 1, 1, 0);
			} else if (cd == 100) {
				if (this.mage.level() instanceof ServerLevel server) {
					LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(server);
					entityToSpawn.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(x, y, z)));
					entityToSpawn.setVisualOnly(true);
					server.addFreshEntity(entityToSpawn);
				}
				final Vec3 _center = new Vec3(x, y, z);
				List<LivingEntity> _entfound = this.mage.level().getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(4), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
				for (LivingEntity living : _entfound) {
					if (living != this.mage) {
						if (!(living instanceof OwnableEntity owned && owned.getOwner() == this.mage))
							this.mage.doHurtTarget(living);
					}
				}
			}
		}

		@Override
		public void stop() {
		}
	}

	static class ThunderShieldGoal extends Goal {
		private final FatuiElectroCicinMageEntity mage;
		private int time = 20;

		public ThunderShieldGoal(FatuiElectroCicinMageEntity mage) {
			this.mage = mage;
		}

		@Override
		public boolean canUse() {
			return mage.getTarget() != null && mage.globalCd <= 0 && mage.shieldCd <= 0;
		}

		@Override
		public boolean canContinueToUse() {
			if (mage instanceof ErEntityInterface enti)
				return mage.getTarget() != null && !enti.er$getShields().isEmpty() || time > 0;
			return false;
		}

		@Override
		public void start() {
			time = 20;
			mage.globalCd = 10;
		}

		@Override
		public void tick() {
			time--;
			if (time == 1) {
				if (mage.globalCd <= 0 && mage instanceof ErEntityInterface enti) {
                    ShieldStack shield = new ShieldStack(ShieldRegistry.THUNDER_SHIELD.get(), 5, 240);
					enti.er$addShield(shield);
					for (LivingEntity i : mage.summonings) {
						i.kill();
					}
				}
			} else if (time == 0) {
				mage.globalCd = 10;
			}
		}

		@Override
		public void stop() {
			mage.shieldCd = 800;
		}
	}
}