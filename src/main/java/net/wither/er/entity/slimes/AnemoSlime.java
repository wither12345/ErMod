package net.wither.er.entity.slimes;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.abs;
import static net.minecraft.network.syncher.EntityDataSerializers.BOOLEAN;
import static net.minecraft.network.syncher.SynchedEntityData.defineId;
import static net.minecraft.world.damagesource.DamageTypes.FALL;
import static net.minecraft.world.damagesource.DamageTypes.MOB_ATTACK;
import static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.world.phys.Vec3.ZERO;
import static net.wither.er.init.ElementRegistry.ANEMO;
import static net.wither.er.elements.ElementSource.createDamageSource;

public class AnemoSlime extends ElementalSlime {
    private static final EntityDataAccessor<Boolean> DATA_INFLATES = defineId(AnemoSlime.class, BOOLEAN);
    private float inflateScaling;

    public AnemoSlime(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    Element getElement() {
        return ANEMO.get();
    }


    public static Builder createAttributes() {
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
    public boolean hurt(@NotNull DamageSource source, float damage) {
        if (source.is(FALL))
            return false;
        return super.hurt(source, this.isInflates() ? 2147483647 : damage);
    }

    @Override
    boolean isTiny() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ElementalSlimeFloatGoal(this));
        this.goalSelector.addGoal(2, new AnemoSlimeAttackGoal(this));
        this.goalSelector.addGoal(4, new ElementalSlimeRandomDirectionGoal(this, 0));
        this.goalSelector.addGoal(5, new ElementalSlimeKeepOnJumpingGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (p_352812_) -> abs(p_352812_.getY() - this.getY()) <= 4.0));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isInflates())
            inflateScaling = 1;
        else if (inflateScaling < 2) {
            inflateScaling += 0.2f;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_INFLATES, false);
    }

    public void setInflates(boolean flag) {
        this.entityData.set(DATA_INFLATES, flag);
    }

    public boolean isInflates() {
        return this.entityData.get(DATA_INFLATES);
    }

    public float getInflateScaling() {
        return this.inflateScaling;
    }

    static class AnemoSlimeAttackGoal extends ElementalSlimeAttackGoal {
        final AnemoSlime slime;

        public AnemoSlimeAttackGoal(AnemoSlime slime) {
            super(slime);
            this.slime = slime;
        }

        @Override
        public void start() {
            super.start();
            this.slime.setInflates(true);
            this.slime.setNoGravity(true);
        }

        public void tick() {
            MoveControl var3 = this.slime.getMoveControl();
            if (var3 instanceof ElementalSlimeMoveControl move_control) {
                move_control.setDirection(this.slime.getYRot());
            }
            this.timer--;
            if (this.timer > 0) {
                this.slime.lookAt(target, 20, 20);
                if (this.timer > 12)
                    this.slime.setDeltaMovement(0, 0.1, 0);
            } else if (this.timer == 0) {
                this.slime.setInflates(false);
                this.slime.setDeltaMovement(this.slime.getLookAngle().multiply(1, 0, 1).normalize().multiply(1.25, 0, 1.25));
            } else if (this.slime.distanceToSqr(target) < 2 && !bumped) {
                this.slime.setNoGravity(false);
                bumped = true;
                this.target.hurt(
                        createDamageSource(this.slime.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(MOB_ATTACK),
                                this.slime,
                                new ElementSource(this.slime.getElement(), new ResourceLocation("er:mob_attack"), 1, true)), (float) this.slime.getAttributeValue(ATTACK_DAMAGE) * 0.5f
                );
                this.slime.setDeltaMovement(ZERO);
            } else if (!bumped && this.timer < -5) {
                this.slime.setNoGravity(false);
                this.slime.setDeltaMovement(this.slime.getDeltaMovement().multiply(-1, 1, -1));
                this.bumped = true;
            }
        }
    }
}
