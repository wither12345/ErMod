package net.wither.er.entity.slimes;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.elements.Element;
import net.wither.er.init.ElementRegistry;
import net.wither.er.elements.ElementSource;

import java.util.Comparator;
import java.util.List;

import static net.wither.er.init.ElementRegistry.PYRO;

public class PyroSlime extends ElementalSlime{
    public PyroSlime(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RechargeElementalGoal(this, 20, 2, 2, 100));
    }

    @Override
    protected void tickDeath() {
        if(this.deathTime == 19 && this.hasElement()){
            this.explode() ;
        }
        super.tickDeath();
    }

    @Override
    public boolean canStandOnFluid(FluidState state) {
        return state.is(FluidTags.LAVA) || super.canStandOnFluid(state) ;
    }

    private void explode(){
        if (this.level() instanceof ServerLevel _level)
            _level.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        if (!level().isClientSide()) {
            level().playSound(null, this.getOnPos(), BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.generic.explode")), SoundSource.NEUTRAL, 1, 1);
        } else {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.generic.explode")), SoundSource.NEUTRAL, 1, 1, false);
        }
        final Vec3 _center = this.position();
        List<LivingEntity> entities= this.level().getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (LivingEntity iterator : entities) {
            if (EntityHurtEvent.shouldHurt(this, iterator)) {
                iterator.hurt(
                        ElementSource.createDamageSource(
                                this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),
                                this,
                                new ElementSource(PYRO.get(), new ResourceLocation("er.slime.explode"), 2, true)
                        ), (float) (2 * this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
                iterator.setDeltaMovement(new Vec3((iterator.getDeltaMovement().x() * 3), (iterator.getDeltaMovement().y() * 2), (iterator.getDeltaMovement().z() * 3)));
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damagesource, float amount) {
        if (damagesource.is(DamageTypes.IN_FIRE))
            return false;
        return super.hurt(damagesource, amount);
    }

    @Override
    Element getElement() {
        return ElementRegistry.PYRO.get();
    }

    @Override
    boolean isTiny() {
        return true;
    }
}
