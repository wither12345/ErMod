package net.wither.er.entity.hypostasiscube;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.entity.TrounceBlossomEntity;
import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.procedures.ApplyErlevelProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.wither.er.client.renderer.hypostasiscube.HypostasisCubeState;
import net.wither.er.init.SerializerRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class HypostasisCube extends Monster {
    private static final EntityDataAccessor<Integer> ANIMATION = SynchedEntityData.defineId(HypostasisCube.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<HypostasisCubeState.Turning>> OPTIONAL_TURNING =
            SynchedEntityData.defineId(HypostasisCube.class, SerializerRegister.TURNING_SERIALIZER.get());
    public static final EntityDataAccessor<Integer> DATA_OMEN_LEVEL = SynchedEntityData.defineId(HypostasisCube.class, EntityDataSerializers.INT);

    private final int[] animateTime = {0, 0};
    protected int respawnCounter = 3;
    @NotNull private HypostasisCubeState animate = HypostasisCubeState.NORMAL;

    protected HypostasisCube(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public boolean canTurn(){
        return this.getTurning() == null && this.getAnimateTime(true) > 20;
    }

    public int getAnimateTime(boolean later){
        return later ? Math.min(this.animateTime[0], this.animateTime[1]) : Math.max(this.animateTime[0], this.animateTime[1]);
    }

    public void refreshTime(){
        if(this.animateTime[0] > this.animateTime[1])
            this.animateTime[0] = 0;
        else this.animateTime[1] = 0;
    }

    public void turnTo(@Nullable HypostasisCubeState state, int time){
        if(!this.level().isClientSide()) {
            if(state == null)
                this.entityData.set(OPTIONAL_TURNING, Optional.empty());
            else {
                this.refreshTime();
                this.entityData.set(OPTIONAL_TURNING, Optional.of(new HypostasisCubeState.Turning(state, time)));
            }
        }
    }

    public @Nullable HypostasisCubeState.Turning getTurning() {
        return this.entityData.get(OPTIONAL_TURNING).orElse(null);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if(source.is(DamageTypes.FALL)) return false;
        if(this.getTurning() != null){
            if(this.getTurning().post().canHurt())
                return super.hurt(source, amount);
        }
        else if(this.animate.canHurt())
            return super.hurt(source, amount);
        return false;
    }

    public void decline(){
        this.respawnCounter --;
    }

    public abstract void tryRespawn();

    public abstract LootTable getLoot();

    public void onDeath(){
        if(this.level() instanceof ServerLevel serverLevel) {
            TrounceBlossomEntity trounceBlossom = ErModEntities.TROUNCE_BLOSSOM.get().spawn(serverLevel, BlockPos.containing(this.getX(), this.getY(), this.getZ()), MobSpawnType.MOB_SUMMONED);
            if (trounceBlossom != null) {
                trounceBlossom.setLootTable(this.getLoot());
                trounceBlossom.setOmenLevel(this.entityData.get(DATA_OMEN_LEVEL));
                ApplyErlevelProcedure.execute(trounceBlossom, this.getPersistentData().getInt("erLevel"));
                ApplyErlevelProcedure.execute(trounceBlossom, EntityHurtEvent.getEntityLevel(this));
            }
        }
    }

    public boolean isAnimate(HypostasisCubeState state){
        if(this.getTurning() != null) return this.getTurning().post() == state;
        return this.animate == state;
    }

    @Override
    public void die(@NotNull DamageSource source) {
        if(this.respawnCounter > 0) {
            this.setHealth(1);
            if(!this.isAnimate(HypostasisCubeState.RESPAWN)) {
                this.turnTo(HypostasisCubeState.RESPAWN, 3);
                this.tryRespawn();
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.respawnCounter <= 0) {
            this.onDeath();
            this.discard();
        }

        this.keepDistance();

        this.animateTime[0] ++ ;
        this.animateTime[1] ++ ;

        HypostasisCubeState.Turning turning = this.getTurning();
        if(turning != null && this.getAnimateTime(true) >= turning.usingTick() && !this.level().isClientSide()) {
            this.animate = turning.post();
            this.entityData.set(ANIMATION, this.animate.ordinal());
            this.turnTo(null, 0);
        }

        if(this.getTurning() == null && !this.level().isClientSide){
            switch (this.animate){
                case RESPAWN -> {
                    if(this.getAnimateTime(true) >= 300) {
                        this.turnTo(HypostasisCubeState.COMBAT, 10);
                        this.setHealth(this.getMaxHealth() * this.respawnCounter * 0.15f);
                    }
                }
                case EMPTY -> {
                    if(this.getAnimateTime(true) >= 100) {
                        if (this.getTarget() == null)
                            this.turnTo(HypostasisCubeState.NORMAL, 10);
                        else
                            this.turnTo(HypostasisCubeState.COMBAT, 10);
                    }
                }
                case NORMAL -> {
                    if(this.getAnimateTime(true) % 20 == 19 && this.getTarget() != null)
                        this.turnTo(HypostasisCubeState.COMBAT, 10);
                }
                case COMBAT -> {
                    if(this.getAnimateTime(true) % 20 == 19 && this.getTarget() == null)
                        this.turnTo(HypostasisCubeState.NORMAL, 10);
                }
            }
        }
    }

    private void keepDistance(){
        double desiredHeight = this.getYPos();
        double tolerance = 0.05;

        Vec3 position = this.position();
        Vec3 start = position.add(0, -this.getBbHeight() / 2, 0);
        Vec3 end = start.add(0, -desiredHeight - 1, 0);

        ClipContext context = new ClipContext(
                start, end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
        );
        BlockHitResult hit = this.level().clip(context);

        if (hit.getType() != HitResult.Type.BLOCK) {
            this.setDeltaMovement(0, -0.2, 0);
            return;
        }

        double groundY = hit.getLocation().y;
        double targetY = groundY + desiredHeight + this.getBbHeight() / 2;

        double diff = targetY - this.getY() - 1;

        if (Math.abs(diff) < tolerance) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0, this.getDeltaMovement().z);
            return;
        }

        double speed = diff * 0.2;
        double maxSpeed = 0.15;
        speed = Math.max(-maxSpeed, Math.min(maxSpeed, speed));

        this.setDeltaMovement(
                this.getDeltaMovement().x,
                speed,
                this.getDeltaMovement().z
        );
    }

    public double getYPos(){
        if(this.getTurning() != null)
            return this.getTurning().post().getYPos();
        return this.animate.getYPos();
    }

    public HypostasisCubeState getState(){
        return this.animate;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("respawn", this.respawnCounter);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.contains("respawn"))
            this.respawnCounter = compoundTag.getInt("respawn");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION, 0);
        builder.define(OPTIONAL_TURNING, Optional.empty());
        builder.define(DATA_OMEN_LEVEL, 0);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
        if(accessor == OPTIONAL_TURNING && entityData.get(OPTIONAL_TURNING).isPresent())
            this.refreshTime();
        if(accessor == ANIMATION)
            this.animate = HypostasisCubeState.byInt(entityData.get(ANIMATION));
        super.onSyncedDataUpdated(accessor);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add(Attributes.MAX_HEALTH, 200);
        builder.add(Attributes.ATTACK_DAMAGE, 8);
        builder.add(Attributes.GRAVITY, 0);
        builder.add(Attributes.KNOCKBACK_RESISTANCE, 100);
        return builder;
    }
}
