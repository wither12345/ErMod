package net.wither.er.entity.hypostasiscube;

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

public class ElectroCubeProjectile extends AbstractHurtingProjectile {
    public ElectroCubeProjectile(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ElectroCubeProjectile(@NotNull Entity owner, @NotNull Entity target, Vec3 pos){
        this(ErModEntities.ELECTRO_CUBE_PROJECTILE.get(), owner.level());
        this.setOwner(owner);
        this.moveTo(pos);
        Vec3 shootVec = target.position().subtract(pos).normalize().scale(0.6);
        this.setDeltaMovement(shootVec);
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
                                            new ElementSource(ElementRegistry.ELECTRO.get(), ResourceLocation.parse("er:cube.projectile"), 2, true)
                                    ), (float) dmg * 0.25f)
                    );

            BlockPos blockPos = this.getOnPos();
            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++)
                    for (int k = -1; k <= 1; k++) {
                        BlockPos offPos = blockPos.offset(i, j, k);
                        if (level.getBlockState(offPos).getBlock() instanceof IBlockBehavior blockBehavior) {
                            blockBehavior.er$getReactionBehavior().ifPresent(
                                    behavior -> behavior.reacted(level, offPos, ElementRegistry.ELECTRO.get(), this.getOwner(), false)
                            );
                        }
                    }
            this.discard();
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
