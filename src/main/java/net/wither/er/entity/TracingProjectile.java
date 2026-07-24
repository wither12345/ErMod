package net.wither.er.entity;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public abstract class TracingProjectile extends Projectile {
    private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(TracingProjectile.class, EntityDataSerializers.INT);
    @Nullable private UUID targetUUID;
    @Nullable private Entity cachedTarget;

    protected int surviveTime = 0;


    public TracingProjectile(EntityType<? extends TracingProjectile> type, Level world) {
        super(type, world);
    }

    @Override
    public boolean canHitEntity(@NotNull Entity entity){
        return this.getTarget() == entity ;
    }

    public void tick() {
        this.surviveTime ++ ;
        Entity target = this.getTarget();
        if (target == null) {
            if(this.level() instanceof ServerLevel)
                this.setTarget(findTarget(12));
        }
        else
            modifyAngle(target);

        Vec3 motion = this.getDeltaMovement();
        this.move(MoverType.SELF, motion);

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity, ClipContext.Block.COLLIDER);

        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }

        if(this.surviveTime > 360)
            this.discard();
    }

    public void modifyAngle(Entity target){
        int maxR = surviveTime / 20 + 5;
        this.lookAt(target, maxR * 4, maxR);
        this.setDeltaMovement(this.getLookAngle().scale(0.5));
    }

    public void lookAt(Entity entity, float y, float x) {
        double d0 = entity.getX() - this.getX();
        double d2 = entity.getZ() - this.getZ();
        double d1;
        if (entity instanceof LivingEntity livingentity) {
            d1 = livingentity.getEyeY() - this.getEyeY();
        } else {
            d1 = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / (double)2.0F - this.getEyeY();
        }

        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float f = (float)(Mth.atan2(d2, d0) * (double)180.0F / (double)(float)Math.PI) - 90.0F;
        float f1 = (float)(-(Mth.atan2(d1, d3) * (double)180.0F / (double)(float)Math.PI));
        this.setXRot(rotLerp(this.getXRot(), f1, x));
        this.setYRot(rotLerp(this.getYRot(), f, y));
    }

    private static float rotLerp(float p_21377_, float p_21378_, float p_21379_) {
        float f = Mth.wrapDegrees(p_21378_ - p_21377_);
        if (f > p_21379_)
            f = p_21379_;

        if (f < -p_21379_)
            f = -p_21379_;
        return p_21377_ + f;
    }

    @Nullable public LivingEntity findTarget(int range){
        Entity owner = this.getOwner() ;
        Optional<LivingEntity> found = this.level().getEntitiesOfClass(LivingEntity.class, AABB.ofSize(this.getPosition(0), range, range, range), e -> true).stream()
                .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(this.getX(), this.getY(), this.getZ())))
                .filter(LivingEntity::isAlive)
                .filter(e -> EntityHurtEvent.shouldHurt(e,owner))
                .filter(e -> !(e instanceof BloomEntityEntity))
                .findFirst();
        return found.orElse(null);
    }

    public void setTarget(@Nullable Entity target) {
        this.entityData.set(TARGET, target == null ? 0 : target.getId() + 1);
        if (target != null) {
            this.targetUUID = target.getUUID();
            this.cachedTarget = target;
        }
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> dataAccessor) {
        if (TARGET.equals(dataAccessor)) {
            int i = this.getEntityData().get(TARGET);
            if(i > 0){
                this.cachedTarget = this.level().getEntity(i - 1);
                if(this.cachedTarget != null)
                    this.targetUUID = this.cachedTarget.getUUID();
            }
            else {
                this.cachedTarget = null;
                this.targetUUID = null;
            }
        }

        super.onSyncedDataUpdated(dataAccessor);
    }

    @Nullable
    public Entity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        } else {
            if (this.targetUUID != null) {
                Level var2 = this.level();
                if (var2 instanceof ServerLevel serverlevel) {
                    this.cachedTarget = serverlevel.getEntity(this.targetUUID);
                    return this.cachedTarget;
                }
            }
            return null;
        }
    }

    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        if (this.targetUUID != null) {
            tag.putUUID("Owner", this.targetUUID);
        }
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Owner")) {
            this.targetUUID = tag.getUUID("Owner");
            this.cachedTarget = null;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(TARGET, 0);
    }
}
