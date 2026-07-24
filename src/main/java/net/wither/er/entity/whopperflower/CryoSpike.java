package net.wither.er.entity.whopperflower;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.elements.ElementSource;
import net.wither.er.init.ElementRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class CryoSpike extends Entity implements TraceableEntity {
    @Nullable
    private UUID ownerUUID;
    @Nullable private Entity cachedOwner;
    public int time;
    public CryoSpike(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {

    }

    public CryoSpike(Vec3 pos, Entity owner) {
        super(ErModEntities.CRYO_SPIKE.get(), owner.level());
        this.setOwner(owner);
        this.setYRot(owner.getYRot());
        this.setPos(pos);
    }

    @Override
    public void tick() {
        super.tick();
        this.time ++ ;
        Level level = this.level();
        final Vec3 center = this.position();
        if(level.isClientSide() && this.time <= 10){
            level.addParticle(ParticleTypes.SNOWFLAKE, center.x + Math.random() - 0.5, center.y + Math.random() * 0.8, center.z + Math.random() - 0.5, 0 , 0, 0);
        }
        if(this.time == 6){
            Entity owner = this.getOwner();
            double dmg ;
            if(owner instanceof LivingEntity living){
                AttributeInstance instance = living.getAttribute(Attributes.ATTACK_DAMAGE);
                dmg = instance == null ? 1 : instance.getValue();
            }
            else dmg = 1;
            level().getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(1), e -> true).stream()
                    .filter(e -> EntityHurtEvent.shouldHurt(e, owner) && e.hurtTime <= 0)
                    .forEach(
                            entity -> entity.hurt(
                                    ElementSource.createDamageSource(
                                            level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),
                                            owner,
                                            new ElementSource(ElementRegistry.CRYO.get(), new ResourceLocation("er:cryo.spike"), 2, true)
                                    ), (float) dmg)
                    );
        }
        if(this.time >= 20)
            this.discard();
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {}


    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUUID = entity.getUUID();
            this.cachedOwner = entity;
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else {
            if (this.ownerUUID != null) {
                Level var2 = this.level();
                if (var2 instanceof ServerLevel serverlevel) {
                    this.cachedOwner = serverlevel.getEntity(this.ownerUUID) instanceof LivingEntity living ? living : null;
                    return this.cachedOwner;
                }
            }
            return null;
        }
    }
}
