package net.wither.er.entity.whopperflower;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.wither.er.block.IBlockBehavior;
import net.wither.er.elements.ElementSource;
import net.wither.er.init.ElementRegistry;
import org.jetbrains.annotations.NotNull;

public class PyroWhopperflowerBall extends AbstractHurtingProjectile {
    public PyroWhopperflowerBall(EntityType<? extends PyroWhopperflowerBall> type, Level level) {
        super(type, level);
    }

    public PyroWhopperflowerBall(Whopperflower whopperflower, Entity target){
        super(ErModEntities.PYRO_FLOWER_BALL.get(), whopperflower.level());
        Vec3 vec = target.getPosition(0).subtract(whopperflower.getPosition(0));
        this.setOwner(whopperflower);
        this.setPos(whopperflower.getEyePosition(1));
        this.setDeltaMovement(vec.multiply(1,0,1).normalize().scale(0.6));
    }

    @Override
    public void tick() {
        this.push(0, -0.01, 0);
        super.tick();
    }

    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        Level level = this.level();
        double dmg;
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity living) {
            AttributeInstance instance = living.getAttribute(Attributes.ATTACK_DAMAGE);
            dmg = instance == null ? 4 : instance.getValue();
        }
        else dmg = 4;
        if (!level.isClientSide) {
            final Vec3 _center = this.getPosition(0);
            level.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream()
                    .filter(e -> EntityHurtEvent.shouldHurt(e, owner))
                    .forEach(
                            entity -> entity.hurt(
                                    ElementSource.createDamageSource(
                                            level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),
                                            owner,
                                            new ElementSource(ElementRegistry.PYRO.get(), new ResourceLocation("er:pyro.ball"), 2, true)
                                    ), (float) dmg * 0.4f)
                    );


            BlockPos blockPos = this.getOnPos();
            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++)
                    for (int k = -1; k <= 1; k++) {
                        BlockPos offPos = blockPos.offset(i, j, k);
                        if (level.getBlockState(offPos).getBlock() instanceof IBlockBehavior blockBehavior) {
                            blockBehavior.er$getReactionBehavior().ifPresent(
                                    behavior -> behavior.reacted(level, offPos, ElementRegistry.PYRO.get(), this.getOwner(), false)
                            );
                        }
                    }
            this.discard();
        }
    }
}
