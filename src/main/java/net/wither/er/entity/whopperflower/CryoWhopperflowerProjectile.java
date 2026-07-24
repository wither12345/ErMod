package net.wither.er.entity.whopperflower;

import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class CryoWhopperflowerProjectile extends AbstractHurtingProjectile {
    public CryoWhopperflowerProjectile(EntityType<CryoWhopperflowerProjectile> type, Level world) {
        super(type, world);
    }

    public CryoWhopperflowerProjectile(Whopperflower whopperflower){
        super(ErModEntities.CRYO_WHOPPERFLOWER_PROJECTILE.get(), whopperflower.level());
        this.setOwner(whopperflower);
        this.setPos(whopperflower.getEyePosition().add(0, -0.5, 0));
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        double dmg;
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity living) {
            AttributeInstance instance = living.getAttribute(Attributes.ATTACK_DAMAGE);
            dmg = instance == null ? 1 : instance.getValue();
            result.getEntity().hurt(owner.damageSources().mobProjectile(this, living), (float) dmg * 0.2f);
        } else {
            result.getEntity().hurt(new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_PROJECTILE)), 1);
        }
        this.discard();
    }
    
    @Override
    protected float getInertia() {
        return 1;
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.discard();
    }

    @Override
    protected @NotNull ParticleOptions getTrailParticle() {
        return ParticleTypes.SNOWFLAKE;
    }
}
