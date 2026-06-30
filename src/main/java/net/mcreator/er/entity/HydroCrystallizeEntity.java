package net.mcreator.er.entity;

import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.common.ForgeMod;

import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;

import net.mcreator.er.procedures.HydroCrystallizeTickProcedure;
import net.mcreator.er.init.ErModEntities;

public class HydroCrystallizeEntity extends PathfinderMob {
	public HydroCrystallizeEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ErModEntities.HYDRO_CRYSTALLIZE.get(), world);
	}

	public HydroCrystallizeEntity(EntityType<HydroCrystallizeEntity> type, Level world) {
		super(type, world);
		setMaxUpStep(0f);
		xpReward = 0;
		setNoAi(true);
		setPersistenceRequired();
		this.setPathfindingMalus(BlockPathTypes.WATER, 0);
		this.moveControl = new MoveControl(this) {
			@Override
			public void tick() {
				if (HydroCrystallizeEntity.this.isInWater())
					HydroCrystallizeEntity.this.setDeltaMovement(HydroCrystallizeEntity.this.getDeltaMovement().add(0, 0.005, 0));
				if (this.operation == MoveControl.Operation.MOVE_TO && !HydroCrystallizeEntity.this.getNavigation().isDone()) {
					double dx = this.wantedX - HydroCrystallizeEntity.this.getX();
					double dy = this.wantedY - HydroCrystallizeEntity.this.getY();
					double dz = this.wantedZ - HydroCrystallizeEntity.this.getZ();
					float f = (float) (Mth.atan2(dz, dx) * (double) (180 / Math.PI)) - 90;
					float f1 = (float) (this.speedModifier * HydroCrystallizeEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
					HydroCrystallizeEntity.this.setYRot(this.rotlerp(HydroCrystallizeEntity.this.getYRot(), f, 10));
					HydroCrystallizeEntity.this.yBodyRot = HydroCrystallizeEntity.this.getYRot();
					HydroCrystallizeEntity.this.yHeadRot = HydroCrystallizeEntity.this.getYRot();
					if (HydroCrystallizeEntity.this.isInWater()) {
						HydroCrystallizeEntity.this.setSpeed((float) HydroCrystallizeEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
						float f2 = -(float) (Mth.atan2(dy, (float) Math.sqrt(dx * dx + dz * dz)) * (180 / Math.PI));
						f2 = Mth.clamp(Mth.wrapDegrees(f2), -85, 85);
						HydroCrystallizeEntity.this.setXRot(this.rotlerp(HydroCrystallizeEntity.this.getXRot(), f2, 5));
						float f3 = Mth.cos(HydroCrystallizeEntity.this.getXRot() * (float) (Math.PI / 180.0));
						HydroCrystallizeEntity.this.setZza(f3 * f1);
						HydroCrystallizeEntity.this.setYya((float) (f1 * dy));
					} else {
						HydroCrystallizeEntity.this.setSpeed(f1 * 0.05F);
					}
				} else {
					HydroCrystallizeEntity.this.setSpeed(0);
					HydroCrystallizeEntity.this.setYya(0);
					HydroCrystallizeEntity.this.setZza(0);
				}
			}
		};
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new WaterBoundPathNavigation(this, world);
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public boolean hurt(DamageSource damagesource, float amount) {
		if (damagesource.is(DamageTypes.IN_FIRE))
			return false;
		if (damagesource.getDirectEntity() instanceof AbstractArrow)
			return false;
		if (damagesource.getDirectEntity() instanceof Player)
			return false;
		if (damagesource.getDirectEntity() instanceof ThrownPotion || damagesource.getDirectEntity() instanceof AreaEffectCloud)
			return false;
		if (damagesource.is(DamageTypes.FALL))
			return false;
		if (damagesource.is(DamageTypes.CACTUS))
			return false;
		if (damagesource.is(DamageTypes.DROWN))
			return false;
		if (damagesource.is(DamageTypes.LIGHTNING_BOLT))
			return false;
		if (damagesource.is(DamageTypes.EXPLOSION) || damagesource.is(DamageTypes.PLAYER_EXPLOSION))
			return false;
		if (damagesource.is(DamageTypes.TRIDENT))
			return false;
		if (damagesource.is(DamageTypes.FALLING_ANVIL))
			return false;
		if (damagesource.is(DamageTypes.DRAGON_BREATH))
			return false;
		if (damagesource.is(DamageTypes.WITHER) || damagesource.is(DamageTypes.WITHER_SKULL))
			return false;
		return super.hurt(damagesource, amount);
	}

	@Override
	public boolean ignoreExplosion() {
		return true;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		HydroCrystallizeTickProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader world) {
		return world.isUnobstructed(this);
	}

	@Override
	public boolean canBreatheUnderwater() {
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		Level world = this.level();
		Entity entity = this;
		return true;
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(ForgeMod.SWIM_SPEED.get(), 0);
		return builder;
	}
}