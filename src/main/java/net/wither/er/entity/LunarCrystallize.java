package net.wither.er.entity;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LunarCrystallize extends Entity implements TraceableEntity {
    private int activeTime = 0;
    private int count = 1;
    private int shootCount ;
    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    public LunarCrystallize(EntityType<?> type, Level level) {
        super(type, level);
    }

    public boolean add(Entity entity){
        if(EntityHurtEvent.shouldHurt(entity, this.getOwner()))
            return false;
        this.activeTime = 0;
        this.count ++;
        if(this.count >= 3) {
            this.count = 0;
            shootCount = 3;
        }
        return true;
    }

    public void modifyPos(){
        while (this.level().getBlockState(this.getOnPos().above()).isSolid())
            this.move(MoverType.SELF, new Vec3(0, 1, 0));
    }

    private void shoot(ServerLevel level, int i){
        double x = 2.5 * Math.cos(i * Math.PI * 2 / 3) + this.getX();
        double z = 2.5 * Math.sin(i * Math.PI * 2 / 3) + this.getZ();
        LunarCrystallizeProjectile projectile = ErModEntities.LUNAR_CRYSTALLIZE_PROJECTILE.get().spawn(level, this.getOnPos(), MobSpawnType.MOB_SUMMONED);
        Entity owner = this.getOwner();
        if(projectile != null) {
            projectile.setOwner(owner);
            projectile.moveTo(x, this.getY(), z);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        if (compoundTag.hasUUID("Owner")) {
            this.ownerUUID = compoundTag.getUUID("Owner");
            this.cachedOwner = null;
        }
        activeTime = compoundTag.getInt("activeTime");
        count = compoundTag.getInt("count");
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        if (this.ownerUUID != null) {
            compoundTag.putUUID("Owner", this.ownerUUID);
        }

        compoundTag.putInt("activeTime", activeTime);
        compoundTag.putInt("count", count);
    }

    @Override
    public void tick() {
        super.tick();
        activeTime ++ ;
        if(this.level() instanceof ServerLevel serverLevel && shootCount > 0){
            this.shoot(serverLevel, shootCount --);
        }
        if(activeTime > 200 && this.level() instanceof ServerLevel){
            this.discard();
        }
    }
    
    public void setOwner(@Nullable Entity p_37263_) {
        if (p_37263_ != null) {
            this.ownerUUID = p_37263_.getUUID();
            this.cachedOwner = p_37263_;
        }
    }

    @Override
    public @Nullable Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else {
            if (this.ownerUUID != null) {
                Level var2 = this.level();
                if (var2 instanceof ServerLevel serverlevel) {
                    this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
                    return this.cachedOwner;
                }
            }

            return null;
        }
    }
}
