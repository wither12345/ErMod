package net.wither.er.entity.hypostasiscube;

import net.mcreator.er.ErMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.wither.er.client.renderer.hypostasiscube.HypostasisCubeState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ShootingGoal extends Goal {
    private static final ResourceLocation DMG_LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "electro_clamp");
    private final ElectroHypostasisCube cube;
    @Nullable private Entity target ;
    private int timer ;
    public ShootingGoal(ElectroHypostasisCube cube){
        this.cube = cube;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.target = cube.getTarget();
        return this.cube.shootCd <= 0 &&
                this.cube.canTurn() &&
                this.cube.getState() == HypostasisCubeState.COMBAT &&
                this.target != null && this.target.isAlive() &&
                this.target.distanceToSqr(cube) > 16;
    }

    @Override
    public void start() {
        this.timer = 0;
        if (this.target != null) {
            this.cube.getLookControl().setLookAt(this.target);
            this.cube.lookAt(this.target, 180, 180);
        }
        this.cube.turnTo(HypostasisCubeState.SHOOTING, 10);
        super.start();
    }

    @Override
    public void tick() {
        this.timer ++ ;
        cube.setDeltaMovement(new Vec3(0, testPos(this.cube.level(), this.cube.getOnPos()) * 0.05, 0));
        switch (this.timer){
            case 15 -> this.shoot(0);
            case 20 -> {
                this.shoot(1);
                this.shoot(5);
            }
            case 25 -> {
                this.shoot(2);
                this.shoot(6);
            }
            case 30 -> {
                this.shoot(3);
                this.shoot(4);
                this.shoot(7);
            }
        }
    }

    private void shoot(int index){
        Vec3 look = this.cube.getLookAngle();
        Vec3 xVec = new Vec3(look.z, 0, -look.x).normalize();
        Vec3 dVec = new Vec3(0, getDy(index), 0).add(xVec.scale(getDx(index)));
        Vec3 pos = this.cube.position().add(dVec);
        if(this.target != null)
            this.cube.level().addFreshEntity(new ElectroCubeProjectile(this.cube, this.target, pos));
        if(this.cube.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 0, 0, 0, 0, 0);
        }
    }

    private static int testPos(Level level, BlockPos pos){
        for(int i = 1 ; i <= 3 ; i ++){
            if(!level.getBlockState(pos.below(i)).canBeReplaced())
                return 4 - i;
        }
        return 0;
    }

    private static double getDy(int index){
        return switch (index){
            case 0 -> 2.5;
            case 1, 7 -> 1.7;
            case 2, 6 -> 0;
            case 3, 5 -> -1.7;
            default -> -2;
        };
    }

    private static double getDx(int index){
        return switch (index){
            case 0, 4 -> 0;
            case 1, 3 -> 1.7;
            case 2 -> 2.5;
            case 5, 7 -> -1.7;
            default -> -2.5;
        };
    }

    @Override
    public boolean canContinueToUse() {
        return this.timer <= 40;
    }

    @Override
    public void stop() {
        this.cube.turnTo(HypostasisCubeState.EMPTY, 10);
        this.cube.shootCd = 200;
        super.stop();
    }
}
