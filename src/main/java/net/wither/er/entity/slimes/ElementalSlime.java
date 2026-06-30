package net.wither.er.entity.slimes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class ElementalSlime extends Monster implements Enemy {
    private int cd ;
    public float squish;
    public float oSquish;
    public float targetSquish;
    private boolean wasOnGround;

    protected ElementalSlime(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.moveControl = new ElementalSlimeMoveControl(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ElementalSlimeFloatGoal(this));
        this.goalSelector.addGoal(2, new ElementalSlimeAttackGoal(this));
        this.goalSelector.addGoal(4, new ElementalSlimeRandomDirectionGoal(this, 1));
        this.goalSelector.addGoal(5, new ElementalSlimeKeepOnJumpingGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (p_352812_) -> Math.abs(p_352812_.getY() - this.getY()) <= 4.0));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }


    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 12);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0);
        return builder;
    }

    @Override
    public void tick() {
        this.squish += (this.targetSquish - this.squish) * 0.5F;
        this.oSquish = this.squish;
        super.tick();
        this.cd -- ;
        if (this.onGround() && !this.wasOnGround) {
            this.targetSquish = -0.5F;
        } else if (!this.onGround() && this.wasOnGround) {
            this.targetSquish = 1.0F;
        }
        this.wasOnGround = this.onGround();
        this.decreaseSquish();
    }

    protected void decreaseSquish() {
        this.targetSquish *= 0.6F;
    }

    public static void init(EntityType<? extends Mob> type) {
        SpawnPlacements.register(type, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));

    }

    boolean hasElement(){
        if(this instanceof AuraContainerInterface containerInterface)
            return containerInterface.getAuraContainer().getAura().get(getElement().getCategory().getId()).hasElement(getElement()) ;
        return false;
    }

    abstract Element getElement();

    protected SoundEvent getJumpSound() {
        return this.isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
    }

    abstract boolean isTiny() ;

    protected float getSoundVolume() {
        return this.isTiny() ? 1 : 0.4f;
    }

    protected int getJumpDelay() {
        return this.random.nextInt(5) + 2;
    }

    float getSoundPitch() {
        float f = this.isTiny() ? 1.4F : 0.8F;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
    }

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    static class ElementalSlimeMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final ElementalSlime slime;
        private boolean active ;
        private Vec3 destination;
        private boolean done ;

        public ElementalSlimeMoveControl(ElementalSlime slime) {
            super(slime);
            this.slime = slime;
            this.active = true;
            this.yRot = 180.0F * slime.getYRot() / 3.1415927F;
        }

        public void setDirection(float yRot) {
            this.yRot = yRot;
        }

        public void setDestination(@NonNull Vec3 destination){
            this.destination = destination;
            this.done = false;
        }

        public void clearDestination(){
            this.destination = null;
            this.done = true;
        }

        public boolean isDone(){
            return this.done || destination == null;
        }

        public void setSpeed(double modifier) {
            this.speedModifier = modifier;
            this.operation = Operation.MOVE_TO;
        }

        public void bump(){
            this.operation = Operation.WAIT ;
        }

        public void setActive(boolean active){
            this.active = active;
        }

        public boolean isActive() {
            return active;
        }

        public void tick() {
            if(!active) {
                super.tick();
            }
            else {
                this.slime.yHeadRot = this.slime.getYRot();
                this.slime.yBodyRot = this.slime.getYRot();
                if (this.operation != Operation.MOVE_TO) {
                    this.slime.setZza(0.0F);
                } else {
                    if (this.slime.onGround()) {
                        if(this.destination != null) {
                            if(done) return;
                            Vec3 delta = this.destination.subtract(this.slime.position());
                            double d0 = delta.x;
                            double d1 = delta.z;
                            double d3 = delta.lengthSqr();
                            if (d3 < 2) {
                                this.mob.setZza(0.0F);
                                this.done = true;
                                return;
                            }
                            float f = (float)(Mth.atan2(d1, d0) * 180.0 / 3.1415927410125732) - 90.0F;
                            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 90.0F));
                        }
                        else {
                            LivingEntity livingentity = this.slime.getTarget();
                            if (livingentity != null) {
                                this.slime.lookAt(livingentity, 45, 45);
                            }
                            else
                                this.slime.setYRot(this.rotlerp(this.slime.getYRot(), this.yRot, 90.0F));
                        }

                        this.slime.setSpeed((float) (this.speedModifier * this.slime.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                        if (this.jumpDelay-- <= 0 && this.slime.getAttributeValue(Attributes.MOVEMENT_SPEED) > 0) {
                            this.jumpDelay = this.slime.getJumpDelay();
                            this.slime.getJumpControl().jump();
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
                        } else {
                            this.slime.xxa = 0.0F;
                            this.slime.zza = 0.0F;
                            this.slime.setSpeed(0.0F);
                        }
                    }
                    else {
                        this.slime.setSpeed((float) (this.speedModifier * this.slime.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    }
                }
            }
        }
    }

    static class ElementalSlimeFloatGoal extends Goal {
        private final ElementalSlime slime;

        public ElementalSlimeFloatGoal(ElementalSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            slime.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof ElementalSlimeMoveControl;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8F) {
                this.slime.getJumpControl().jump();
            }

            MoveControl var2 = this.slime.getMoveControl();
            if (var2 instanceof ElementalSlimeMoveControl move_control) {
                move_control.setSpeed(1.5);
            }

        }
    }

    static class ElementalSlimeAttackGoal extends Goal {
        final ElementalSlime slime;
        int timer;
        boolean bumped ;
        LivingEntity target ;

        public ElementalSlimeAttackGoal(ElementalSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.LOOK,Flag.TARGET,Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity != null && this.slime.getMoveControl() instanceof ElementalSlimeMoveControl control && control.active) {
                this.target = livingentity;
                return this.slime.distanceToSqr(livingentity) < 10 && this.slime.canAttack(livingentity) && this.slime.getMoveControl() instanceof ElementalSlimeMoveControl && this.slime.cd <= 0 && this.slime.onGround();
            } else {
                return false;
            }
        }

        public void start() {
            this.timer = 15;
            this.slime.cd = 60;
            if (this.slime.getMoveControl() instanceof ElementalSlimeMoveControl move_control) {
                move_control.setActive(true);
                move_control.bump();
            }
            this.bumped = false;
            super.start();
        }

        public boolean canContinueToUse() {
            if (target == null) {
                return false;
            } else {
                return this.slime.canAttack(target) && (!this.slime.onGround() || !bumped);
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            MoveControl var3 = this.slime.getMoveControl();
            if (var3 instanceof ElementalSlimeMoveControl move_control) {
                move_control.setDirection(this.slime.getYRot());
            }
            this.timer --;
            if(this.timer > 0)
                this.slime.lookAt(target,20,20);
            else if(this.timer == 0) {
                this.slime.setDeltaMovement(this.slime.getLookAngle().multiply(1,0, 1).normalize().add(0,0.35,0));
            }
            else if(this.slime.distanceToSqr(target) < 1 && !bumped){
                bumped = true;
                if(this.slime.hasElement())
                    this.target.hurt(
                            ElementSource.createDamageSource(this.slime.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),
                            this.slime,
                            new ElementSource(this.slime.getElement(), new ResourceLocation("er:mob_attack"), 1, true)), (float) this.slime.getAttributeValue(Attributes.ATTACK_DAMAGE)
                    );
                else
                    this.target.hurt(new DamageSource(this.slime.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), this.slime), (float) this.slime.getAttributeValue(Attributes.ATTACK_DAMAGE));
                this.slime.setDeltaMovement(Vec3.ZERO);
            }
            else if(!bumped && this.slime.getDeltaMovement().y < 0 && this.timer < -2) {
                this.slime.setDeltaMovement(this.slime.getDeltaMovement().multiply(-1, 1, -1));
                this.bumped = true;
            }
        }
    }

    static class ElementalSlimeRandomDirectionGoal extends Goal {
        private final ElementalSlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;
        protected double wantedX;
        protected double wantedY;
        protected double wantedZ;
        private ElementalSlimeMoveControl control;
        protected final double speedModifier;

        public ElementalSlimeRandomDirectionGoal(ElementalSlime slime, double speedModifier) {
            this.slime = slime;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.LOOK,Flag.MOVE));
        }

        public boolean canUse() {
            if(this.slime.getTarget() == null && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof ElementalSlimeMoveControl moveControl){
                this.control = moveControl ;
                if(!this.control.active){
                    Vec3 vec3 = this.getPosition();
                    if (vec3 == null)
                        return false;
                    this.wantedX = vec3.x;
                    this.wantedY = vec3.y;
                    this.wantedZ = vec3.z;
                    return true;
                }
                return true ;
            }

            return false;
        }

        @Nullable
        protected Vec3 getPosition() {
            return DefaultRandomPos.getPos(this.slime, 10, 7);
        }

        @Override
        public void start() {
            if(!control.active)
                this.slime.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }

        public void stop() {
            this.slime.getNavigation().stop();
        }

        @Override
        public boolean canContinueToUse() {
            return control.active || (!this.slime.getNavigation().isDone() && !this.slime.hasControllingPassenger());
        }

        public void tick() {
            if(control.active) {
                if (--this.nextRandomizeTime <= 0) {
                    this.nextRandomizeTime = this.adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
                    this.chosenDegrees = (float) this.slime.getRandom().nextInt(360);
                }

                MoveControl var2 = this.slime.getMoveControl();
                if (var2 instanceof ElementalSlimeMoveControl move_control) {
                    move_control.setDirection(this.chosenDegrees);
                }
            }
        }
    }

    static class ElementalSlimeKeepOnJumpingGoal extends Goal {
        private final ElementalSlime slime;

        public ElementalSlimeKeepOnJumpingGoal(ElementalSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            return !this.slime.isPassenger();
        }

        public void tick() {
            MoveControl var2 = this.slime.getMoveControl();
            if (var2 instanceof ElementalSlimeMoveControl move_control) {
                move_control.setSpeed(1.2);
            }
        }
    }

    static class RechargeElementalGoal extends Goal{
        private final ElementalSlime slime ;
        private final AuraContainerInterface containerInterface;
        private final int chargeTick ;
        private final double speed;
        private int chargeTime ;
        private final float gauge;
        private final int initCd;
        private int cd ;

        RechargeElementalGoal(ElementalSlime slime, int chargeTick, double speed, float gauge, int initCd) {
            this.slime = slime;
            this.containerInterface = slime instanceof AuraContainerInterface auraContainerInterface ? auraContainerInterface: null;
            this.chargeTick = chargeTick;
            this.speed = speed;
            this.gauge = gauge;
            this.initCd = initCd;
            this.cd = initCd;
            this.setFlags(EnumSet.of(Flag.MOVE,Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if(this.slime.hasElement())
                return false;

            ElementalSlimeMoveControl control = slime.moveControl instanceof ElementalSlimeMoveControl slimeMoveControl ? slimeMoveControl : null;
            LivingEntity target = this.slime.getTarget();
            if (target == null) {
                containerInterface.getAuraContainer().addAura(new ElementSource(this.slime.getElement(), null, gauge, true, true));
                return false;
            } else if(this.cd -- > 0)
                return false;
            else {
                Vec3 vec3 = DefaultRandomPos.getPosAway(this.slime, 12, 7, target.position());
                if (vec3 == null) {
                    return false;
                } else if (target.distanceToSqr(vec3.x, vec3.y, vec3.z) < target.distanceToSqr(this.slime)) {
                    return false;
                } else {
                    if (control != null) {
                        control.setDestination(vec3);
                        control.setSpeed(speed);
                    }
                    return true;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.chargeTime < this.chargeTick;
        }

        @Override
        public void start() {
            this.cd = this.initCd;
            this.chargeTime = 0;
        }

        @Override
        public void stop() {
            ElementalSlimeMoveControl control = slime.moveControl instanceof ElementalSlimeMoveControl slimeMoveControl ? slimeMoveControl : null;
            super.stop();
            if(control != null)
                control.clearDestination();
            containerInterface.getAuraContainer().addAura(new ElementSource(this.slime.getElement(), null, gauge, true, true));
        }

        @Override
        public void tick() {
            ElementalSlimeMoveControl control = slime.moveControl instanceof ElementalSlimeMoveControl slimeMoveControl ? slimeMoveControl : null;
            if(control != null)
                control.setSpeed(speed);
            if (control == null || control.isDone()) {
                this.chargeTime ++;
            }
        }
    }
}
