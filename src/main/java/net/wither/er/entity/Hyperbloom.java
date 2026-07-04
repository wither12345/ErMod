package net.wither.er.entity;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class Hyperbloom extends Projectile {
    @Nullable
    private UUID targetUUID;
    @Nullable
    private Entity cachedTarget;

    private int surviveTime = 0;

    private static final float moving_speed = 1f;
    private static final float max_speed = 0.2f;

    public Hyperbloom(EntityType<Hyperbloom> type, Level world) {
        super(type, world);
    }

    @Override
    public void onHitEntity(@NotNull EntityHitResult result) {
        Entity entity = result.getEntity() ;
        entity.hurt(new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(Element.BLOOM) , this),12 * EntityHurtEvent.getLevelMultiply(this.getOwner()));
        this.discard();
    }

     @Override
     public boolean canHitEntity(Entity entity){
        return this.getTarget() == entity ;
     }

    public void tick() {
        super.tick();
        this.surviveTime ++ ;
        Entity target = this.getTarget();
        if (target != null)
            this.push(target.getPosition(target.getBbHeight() / 2).subtract(this.getPosition(0)).normalize().multiply(moving_speed, moving_speed, moving_speed));
        else this.setTarget(findTarget(6));

        Vec3 vec3 = this.getDeltaMovement();

        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.setPos(d0, d1, d2);

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity, ClipContext.Block.COLLIDER);
        
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }


        if(this.surviveTime > 180)
            this.discard();

    }

    public LivingEntity findTarget(int range){
        Entity owner = this.getOwner() ;
        Optional<LivingEntity> found = this.level().getEntitiesOfClass(LivingEntity.class, AABB.ofSize(this.getPosition(0), range, range, range), e -> true).stream()
                .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(this.getX(), this.getY(), this.getZ())))
                .filter(LivingEntity::isAlive)
                .filter(e -> EntityHurtEvent.shouldHurt(e,this.getOwner()))
                .filter(e -> !(e instanceof BloomEntityEntity))
                .findFirst();
        return found.orElse(null);
    }

    public void setTarget(@Nullable Entity target) {
        if (target != null) {
            this.targetUUID = target.getUUID();
            this.cachedTarget = target;
        }
    }

    @Nullable
    public Entity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        } else {
            if (this.targetUUID != null) {
                Level var2 = this.level();
                if (var2 instanceof ServerLevel) {
                    ServerLevel serverlevel = (ServerLevel)var2;
                    this.cachedTarget = serverlevel.getEntity(this.targetUUID);
                    return this.cachedTarget;
                }
            }

            return null;
        }
    }

    protected void addAdditionalSaveData(CompoundTag p_37265_) {
        if (this.targetUUID != null) {
            p_37265_.putUUID("Owner", this.targetUUID);
        }
    }

    protected void readAdditionalSaveData(CompoundTag p_37262_) {
        if (p_37262_.hasUUID("Owner")) {
            this.targetUUID = p_37262_.getUUID("Owner");
            this.cachedTarget = null;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }
}
