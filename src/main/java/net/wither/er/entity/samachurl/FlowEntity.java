package net.wither.er.entity.samachurl;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.client.multiplayer.ClientLevel;
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
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FlowEntity extends Entity implements TraceableEntity {
    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    private int activeTime = 0;

    private static final float RAT_36 = (float) Math.toRadians(36);

    public FlowEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public FlowEntity(Level level, Vec3 pos, LivingEntity owner){
        super(ErModEntities.FLOW_ENTITY.get(), level);
        this.setPos(pos);
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        super.tick();
        this.activeTime ++;
        if(this.activeTime <= 20 && this.level() instanceof ClientLevel level){
            Vec3 pos = this.position();
            if(activeTime < 20) {
                for (int i = 0; i < 3; i++) {
                    Vec3 pos1 = pos.add(Math.random() - 0.5, 0.1, Math.random() - 0.5);
                    level.addParticle(ParticleTypes.SPLASH, pos1.x(), pos1.y(), pos1.z(), 0, 0, 0);
                }
            }
            else
                for (int i = 0; i < 16; i++) {
                    Vec3 pos1 = pos.add(Math.random() - 0.5, 1, Math.random() - 0.5);
                    level.addParticle(ParticleTypes.SPLASH, pos1.x(), pos1.y(), pos1.z(), 0, 2, 0);
                }
        }
        if(this.activeTime == 20 && this.level() instanceof ServerLevel serverlevel){
            final Vec3 center = this.position();
            double dmg ;
            Entity owner = this.getOwner();
            if(owner instanceof LivingEntity living){
                AttributeInstance instance = living.getAttribute(Attributes.ATTACK_DAMAGE);
                dmg = instance == null ? 1 : instance.getValue();
            }
            else dmg = 1;
            serverlevel.getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(1), e -> EntityHurtEvent.shouldHurt(e, owner) && e.hurtTime <= 0)
                    .forEach(
                            entity -> entity.hurt(
                                    ElementSource.createDamageSource(
                                            serverlevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),
                                            owner,
                                            new ElementSource(ElementRegistry.HYDRO.get(), ResourceLocation.parse("er:hydro.flow"), 2, true)
                                    ), (float) (dmg * 0.3))
                    );
        }
        if(this.activeTime >= 30)
            this.discard();
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

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }
}
