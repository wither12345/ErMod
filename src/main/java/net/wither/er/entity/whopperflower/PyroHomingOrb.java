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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.wither.er.block.IBlockBehavior;
import net.wither.er.elements.ElementSource;
import net.wither.er.entity.TracingProjectile;
import net.wither.er.init.ElementRegistry;
import org.jetbrains.annotations.NotNull;

public class PyroHomingOrb extends TracingProjectile {
    public PyroHomingOrb(EntityType<PyroHomingOrb> type, Level world) {
        super(type, world);
    }

    public PyroHomingOrb(Whopperflower whopperflower, Entity target){
        super(ErModEntities.PYRO_HOMING_ROB.get(), whopperflower.level());
        this.setOwner(whopperflower);
        this.setPos(whopperflower.getEyePosition(1));
        this.setTarget(target);
    }

    @Override
    public void modifyAngle(Entity target){
        int maxR = 5 - Math.min(surviveTime / 10, 5);
        this.lookAt(target, maxR * 3, maxR);
        this.setDeltaMovement(this.getLookAngle().scale(0.4));
    }

    @Override
    public void tick() {
        super.tick();
        this.applyGravity();
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        Level level = this.level();
        double dmg;
        Entity owner = this.getOwner();
        int x = this.getBlockX();
        int y = this.getBlockY();
        int z = this.getBlockZ();
        if (owner instanceof LivingEntity living) {
            AttributeInstance instance = living.getAttribute(Attributes.ATTACK_DAMAGE);
            dmg = instance == null ? 4 : instance.getValue();
        } else {
            dmg = 4;
        }
        if (!level.isClientSide) {
            final Vec3 _center = this.getPosition(0);
            level.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(2), e -> true).stream()
                    .filter(e -> EntityHurtEvent.shouldHurt(e, owner))
                    .forEach(
                            entity -> entity.hurt(
                                    ElementSource.createDamageSource(
                                            level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),
                                            owner,
                                            new ElementSource(ElementRegistry.PYRO.get(), ResourceLocation.parse("er:pyro.orb"), 2, true)
                                    ), (float) dmg * 0.22f)
                    );

            BlockPos blockPos = this.getOnPos();
            for (int i = -2; i <= 2; i++)
                for (int j = -2; j <= 2; j++)
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

    @Override
    protected double getDefaultGravity() {
        return 0.01;
    }
}
