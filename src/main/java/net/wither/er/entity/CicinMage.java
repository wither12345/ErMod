package net.wither.er.entity;

import net.mcreator.er.entity.ElectroCicinEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class CicinMage extends Monster {
	public final List<LivingEntity> summonings = new ArrayList<>();;
	private final List<UUID> cashedSummonings = new ArrayList<>();
	private boolean cashed ;

	public int globalCd = 0;

	public CicinMage(EntityType<? extends CicinMage> type, Level world) {
		super(type, world);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		CompoundTag sumTag = new CompoundTag() ;
		int index = 0 ;
		for(LivingEntity living : summonings){
			sumTag.putUUID(String.valueOf(index ++), living.getUUID());
		}
		tag.put("summonings" , sumTag) ;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if(this.level() instanceof ServerLevel level && tag.contains("summonings")){
			CompoundTag sumTag = tag.getCompound("summonings") ;
			Set<String> keys = sumTag.getAllKeys() ;
			for(String key : keys){
				cashedSummonings.add(sumTag.getUUID(key));
			}
			cashed = true ;
		}
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.generic.hurt"));
	}

	public void transferCash(){
		if(cashed && this.level() instanceof ServerLevel serverLevel){
			for(UUID summoningUUID : cashedSummonings){
				Entity entity = serverLevel.getEntity(summoningUUID) ;
				if(entity instanceof LivingEntity living && living.isAlive())
					summonings.add(living) ;
			}
		}
		cashed = false ;
	}

	protected void blink() {
		if (!this.level().isClientSide() && this.isAlive()) {
			double d0 = this.getX() + (this.random.nextDouble() - 0.5) * 16.0;
			double d1 = this.getY() + (double) (this.random.nextInt(32) - 16);
			double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * 16.0;
			this.blink(d0, d1, d2);
		}
	}

	void blinkTowards(Entity target) {
		Vec3 vec3 = new Vec3(this.getX() - target.getX(), this.getY(0.5) - target.getEyeY(), this.getZ() - target.getZ());
		vec3 = vec3.normalize();
		double d1 = this.getX() + (this.random.nextDouble() - 0.5) * 4.0 - vec3.x * 2.0;
		double d2 = this.getY() + (double) (this.random.nextInt(16) - 8) - vec3.y * 2.0;
		double d3 = this.getZ() + (this.random.nextDouble() - 0.5) * 4.0 - vec3.z * 2.0;
		this.blink(d1, d2, d3);
	}

	private void blink(double x, double y, double z) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(x, y, z);
		while (blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
			blockpos$mutableblockpos.move(Direction.DOWN);
		}
		BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
		boolean flag = blockstate.blocksMotion();
		boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
		if (flag && !flag1) {
			Vec3 vec3 = this.position();
			this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
			this.randomTeleport(x, y, z, true);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (globalCd > 0)
			globalCd--;
	}

	@Override
	public void die(@NotNull DamageSource source) {
		for (LivingEntity i : summonings) {
			i.kill();
		}
		super.die(source);
	}

	static public class MistyCallGoal extends Goal {
		private final CicinMage mage;
		private final EntityType summoning;
		private final double count;
		private LivingEntity target;
		private int coolDown = 0;

		public MistyCallGoal(CicinMage mage, EntityType summoning, int count) {
			this.mage = mage;
			this.summoning = summoning;
			this.count = count;
		}

		@Override
		public boolean canUse() {
			mage.transferCash();
            mage.summonings.removeIf(sum -> sum == null || !sum.isAlive());
			if (mage instanceof ErEntityInterface enti)
				return mage.getTarget() != null && mage.globalCd <= 0 && mage.summonings.size() < count && enti.er$getShieldStacks().isEmpty();
			return false;
		}

		@Override
		public boolean canContinueToUse() {
			return coolDown-- > 0 && mage.globalCd <= 0;
		}

		@Override
		public void start() {
			if (mage.level() instanceof ServerLevel serverlevel) {
				coolDown = 100;
				mage.globalCd = 25;
				int t = mage.summonings.size();
				for (int i = t; i < 3; i++) {
					Mob entityToSpawn = (Mob) summoning.spawn(serverlevel, BlockPos.containing(mage.getX() + Mth.nextDouble(RandomSource.create(), -1, 1), mage.getY() + 2, mage.getZ() + Mth.nextDouble(RandomSource.create(), -1, 1)),
							MobSpawnType.MOB_SUMMONED);
					mage.summonings.add(entityToSpawn);
					if (entityToSpawn instanceof ElectroCicinEntity cicin) {
                        cicin.setTarget(mage.getTarget());
						cicin.setOwnerUUID(mage.getUUID());
					}
				}
			}
		}

		@Override
		public void tick() {
		}

		@Override
		public void stop() {
		}
	}

	static public class KeepDistanceGoal extends Goal {
		private final CicinMage mage;
		private final float distanceSqr;
		private int seeTime;
		private boolean strafingClockwise;
		private boolean strafingBackwards;
		private int strafingTime = -1;

		public KeepDistanceGoal(CicinMage mage, double speedModifier, float distance) {
			this.mage = mage;
			this.distanceSqr = distance * distance;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			return this.mage.getTarget() != null;
		}

		@Override
		public boolean canContinueToUse() {
			return !this.mage.getNavigation().isDone() && this.mage.getTarget() != null && this.mage.getTarget().isAlive();
		}

		@Override
		public void start() {
			super.start();
			this.mage.setAggressive(true);
		}

		@Override
		public void stop() {
			super.stop();
			this.mage.setAggressive(false);
			this.seeTime = 0;
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			LivingEntity livingentity = this.mage.getTarget();
			if (this.mage.getFallFlyingTicks() > 10)
				this.mage.blink();
			if (livingentity != null) {
				double d0 = this.mage.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
				boolean flag = this.mage.getSensing().hasLineOfSight(livingentity);
				boolean flag1 = this.seeTime > 0;
				if (flag != flag1) {
					this.seeTime = 0;
				}
				this.seeTime = 100;
				if (!(d0 > (double) this.distanceSqr)) {
					this.mage.getNavigation().stop();
					this.strafingTime++;
				} else {
					this.mage.blinkTowards(livingentity);
					this.strafingTime = -1;
				}
				if (this.strafingTime >= 20) {
					if ((double) this.mage.getRandom().nextFloat() < 0.3) {
						this.strafingClockwise = !this.strafingClockwise;
					}
					if ((double) this.mage.getRandom().nextFloat() < 0.3) {
						this.strafingBackwards = !this.strafingBackwards;
					}
					this.strafingTime = 0;
				}
				if (this.strafingTime > -1) {
					if (d0 > (double) (this.distanceSqr * 0.75F)) {
						this.strafingBackwards = false;
					} else if (d0 < (double) (this.distanceSqr * 0.25F)) {
						this.strafingBackwards = true;
					}
					this.mage.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
					if (this.mage.getControlledVehicle() instanceof Mob mob) {
						mob.lookAt(livingentity, 30.0F, 30.0F);
					}
					this.mage.lookAt(livingentity, 30.0F, 30.0F);
				} else {
					this.mage.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
				}
			}
		}
	}
}