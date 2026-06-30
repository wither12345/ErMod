package net.wither.er.entity;

import net.mcreator.er.StellaFortunas;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;
import org.jetbrains.annotations.Nullable;

public class EnergyOrb extends Entity {
	public static final EntityDataAccessor<Integer> DATA_Element = SynchedEntityData.defineId(EnergyOrb.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> DATA_Amount = SynchedEntityData.defineId(EnergyOrb.class, EntityDataSerializers.FLOAT);
	private int age;
	private int health = 5;
	private Player followingPlayer;

	public EnergyOrb(EntityType<? extends EnergyOrb> type, Level level) {
		super(type, level);
		this.health = 5;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_Element, 0);
		this.entityData.define(DATA_Amount, 1f);
	}

	public void setType(int element, float amount) {
		this.entityData.set(DATA_Element, element);
		this.entityData.set(DATA_Amount, amount);
	}

	@Override
	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.NONE;
	}

	@Override
	public void tick() {
		super.tick();
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		if (this.isEyeInFluid(FluidTags.WATER)) {
			this.setUnderwaterMovement();
		}
		if (this.level().getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
			this.setDeltaMovement((double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F), 0.2F, (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F));
		}
		if (!this.level().noCollision(this.getBoundingBox())) {
			this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
		}
		if (this.tickCount % 20 == 1) {
			this.scanForEntities();
		}
		if (this.followingPlayer != null && (this.followingPlayer.isSpectator() || this.followingPlayer.isDeadOrDying())) {
			this.followingPlayer = null;
		}
		if (this.followingPlayer != null) {
			Vec3 vec3 = new Vec3(this.followingPlayer.getX() - this.getX(), this.followingPlayer.getY() + (double) this.followingPlayer.getEyeHeight() / 2.0 - this.getY(), this.followingPlayer.getZ() - this.getZ());
			double d0 = vec3.lengthSqr();
			if (d0 < 64.0) {
				double d1 = 1.0 - Math.sqrt(d0) / 8.0;
				this.setDeltaMovement(this.getDeltaMovement().add(vec3.normalize().scale(d1 * d1 * 0.1)));
			}
		}
		this.move(MoverType.SELF, this.getDeltaMovement());
		float f = 0.98F;
		if (this.onGround()) {
			BlockPos pos = getBlockPosBelowThatAffectsMyMovement();
			f = this.level().getBlockState(pos).getFriction(this.level(), pos, this) * 0.98F;
		}
		this.setDeltaMovement(this.getDeltaMovement().multiply((double) f, 0.98, (double) f));
		if (this.onGround()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, -0.9, 1.0));
		}
		this.age++;
		if (this.age >= 6000) {
			this.discard();
		}
	}

	@Override
	public BlockPos getBlockPosBelowThatAffectsMyMovement() {
		return this.getOnPos(0.999999F);
	}

	private void scanForEntities() {
		if (this.followingPlayer == null || this.followingPlayer.distanceToSqr(this) > 64.0) {
			this.followingPlayer = getFollowingPlayer();
		}
	}

	public @Nullable Player getFollowingPlayer() {
		return this.followingPlayer != null ? this.followingPlayer : this.level().getNearestPlayer(this, 8);
	}

	private void setUnderwaterMovement() {
		Vec3 vec3 = this.getDeltaMovement();
		this.setDeltaMovement(vec3.x * 0.99F, Math.min(vec3.y + 5.0E-4F, 0.06F), vec3.z * 0.99F);
	}

	@Override
	protected void doWaterSplashEffect() {
	}

	@Override
	public boolean hurt(DamageSource p_20785_, float p_20786_) {
		if (this.isInvulnerableTo(p_20785_)) {
			return false;
		} else if (this.level().isClientSide) {
			return true;
		} else {
			this.markHurt();
			this.health = (int) ((float) this.health - p_20786_);
			if (this.health <= 0) {
				this.discard();
			}
			return true;
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putShort("Health", (short) this.health);
		compound.putShort("Age", (short) this.age);
		compound.putInt("Element", this.entityData.get(DATA_Element));
		compound.putFloat("Amount", this.entityData.get(DATA_Amount));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.health = compound.getShort("Health");
		this.age = compound.getShort("Age");
		if (compound.contains("Element"))
			this.entityData.set(DATA_Element, compound.getInt("Element"));
		if (compound.contains("Amount"))
			this.entityData.set(DATA_Amount, compound.getFloat("Amount"));
	}

	@Override
	public void playerTouch(Player player) {
		if (this.age <= 15)
			return;
		if (player instanceof ServerPlayer serverplayer) {
			if (player.getCapability(ErItemVariables.PLAYER_VARIABLES).orElse(new ErItemVariables.PlayerVariables()).Stella_Fortuna.getItem() instanceof StellaFortunas fortuna) {
				ErCombatVariables.PlayerVariables vars = serverplayer.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
				vars.energyAmount = Math.min(fortuna.getEnergyCost(player), vars.energyAmount + this.getAmount() * (float) player.getAttribute(ErModAttributes.ENERGY_RECHARGE.get()).getValue() / 100);
				vars.syncPlayerVariables(player);
			}
			this.discard();
		}
	}

	public float getAmount() {
		return this.entityData.get(DATA_Amount);
	}

	public int getElement() {
		return this.entityData.get(DATA_Element);
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Override
	public SoundSource getSoundSource() {
		return SoundSource.AMBIENT;
	}
}