package net.wither.er.entity.samachurl;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.IAuraContainerOwner;
import net.wither.er.init.ElementRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RainCloud extends Entity implements TraceableEntity {
    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    private float healPercent;
    private int activeTime = 0;
    private static final double RADIUS = 3;

    public RainCloud(EntityType<?> type, Level level) {
        super(type, level);
    }

    public RainCloud(Level level, Vec3 pos, LivingEntity owner, float healPercent){
        super(ErModEntities.RAIN_CLOUD.get(), level);
        this.setPos(pos);
        this.setOwner(owner);
        this.healPercent = healPercent;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        activeTime = compoundTag.getInt("activeTime");

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        compoundTag.putInt("activeTime", activeTime);
    }

    @Override
    public void tick() {
        super.tick();
        activeTime ++ ;
        if(this.level().isClientSide()){
            Vec3 pos = this.position();
            for(int i = 0; i < 3; i++){
                pos = pos.add(Math.random() * 3 - 1.5, 0, Math.random() * 3 - 1.5);
                this.level().addParticle(ParticleTypes.FALLING_WATER, pos.x(), pos.y(), pos.z(), 0, 0, 0);
            }
        }
        else {
            if (activeTime > 200 || (this.getOwner() instanceof Player player && !player.getUseItem().is(ErModItems.RAIN_PRAYER)))
                this.discard();


            if (this.activeTime % 20 == 0) {
                Vec3 pos = this.position().add(0, -4, 0);
                this.level().getEntitiesOfClass(LivingEntity.class,
                        new AABB(pos, pos).inflate(RADIUS),
                        e -> (e.distanceToSqr(pos) <= RADIUS * RADIUS) && e.isAlive()
                ).forEach(entity -> {
                    if (!EntityHurtEvent.shouldHurt(entity, this.getOwner()))
                        entity.heal(entity.getMaxHealth() * healPercent);

                    if (entity instanceof IAuraContainerOwner auraOwner) {
                        auraOwner.er$getAuraContainer()
                                .addAura(new ElementSource(ElementRegistry.HYDRO.get(), null, 2, true, true));
                    }
                });
            }
        }
    }

    public void setOwner(@Nullable Entity p_37263_) {
        if (p_37263_ != null) {
            this.ownerUUID = p_37263_.getUUID();
            this.cachedOwner = p_37263_;
        }

    }

    @Override
    public Entity getOwner() {
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
