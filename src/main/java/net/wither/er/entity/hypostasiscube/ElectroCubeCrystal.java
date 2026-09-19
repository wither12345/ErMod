package net.wither.er.entity.hypostasiscube;

import net.mcreator.er.init.ErModEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.wither.er.client.renderer.hypostasiscube.HypostasisCubeState;
import net.wither.er.entity.ElementalLivingEntity;
import net.wither.er.init.ElementRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectroCubeCrystal extends ElementalLivingEntity {
    @Nullable private final HypostasisCube cube;
    private boolean declined = false;

    public ElectroCubeCrystal(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level, ElementRegistry.ELECTRO.get());
        this.cube = null;
    }

    public ElectroCubeCrystal(HypostasisCube owner){
        super(ErModEntities.ELECTRO_CUBE_CRYSTAL.get(), owner.level(), ElementRegistry.ELECTRO.get());
        this.cube = owner;
    }

    @Override
    protected int getMaxAura() {
        return 3;
    }

    @Override
    public void tick() {
        if(!this.level().isClientSide() && (this.cube == null || !this.cube.isAnimate(HypostasisCubeState.RESPAWN)))
            this.discard();
        if(this.getHealth() <= 0 && !this.declined && this.cube != null) {
            this.cube.decline();
            this.declined = true;
        }
        this.keepDistance();
        super.tick();
    }

    private void keepDistance(){
        double desiredHeight = 1.5;
        double tolerance = 0.05;

        Vec3 position = this.position();
        Vec3 start = position.add(0, -this.getBbHeight() / 2, 0);
        Vec3 end = start.add(0, -desiredHeight - 1, 0);

        ClipContext context = new ClipContext(
                start, end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
        );
        BlockHitResult hit = this.level().clip(context);

        if (hit.getType() != HitResult.Type.BLOCK) {
            this.setDeltaMovement(0, -0.2, 0);
            return;
        }

        double groundY = hit.getLocation().y;
        double targetY = groundY + desiredHeight + this.getBbHeight() / 2;

        double diff = targetY - this.getY() - 1;

        if (Math.abs(diff) < tolerance) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0, this.getDeltaMovement().z);
            return;
        }

        double speed = diff * 0.2;
        double maxSpeed = 0.15;
        speed = Math.max(-maxSpeed, Math.min(maxSpeed, speed));

        this.setDeltaMovement(
                this.getDeltaMovement().x,
                speed,
                this.getDeltaMovement().z
        );
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float amount) {
        return super.hurt(damageSource, 0);
    }
}
