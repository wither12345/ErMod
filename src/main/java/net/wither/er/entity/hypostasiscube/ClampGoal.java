package net.wither.er.entity.hypostasiscube;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.client.renderer.hypostasiscube.HypostasisCubeState;
import net.wither.er.elements.ElementSource;
import net.wither.er.init.ElementRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ClampGoal extends Goal {
    private static final ResourceLocation DMG_LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "electro_clamp");
    private final ElectroHypostasisCube cube;
    @Nullable private Entity target ;
    private int timer ;
    public ClampGoal(ElectroHypostasisCube cube){
        this.cube = cube;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.target = cube.getTarget();
        return this.cube.clampCd <= 0 &&
                this.cube.canTurn() &&
                this.cube.getState() == HypostasisCubeState.COMBAT &&
                this.target != null && this.target.isAlive() &&
                this.target.distanceToSqr(cube) <= 16;
    }

    @Override
    public void start() {
        this.timer = 0;
        this.cube.turnTo(HypostasisCubeState.CLAMP, 10);
        super.start();
    }

    @Override
    public void tick() {
        this.timer ++ ;
        if(this.timer == 10 && this.cube.level() instanceof ServerLevel serverLevel) {
            Vec3 look = this.cube.getLookAngle().multiply(1,0,1).normalize();
            Vec3 center = this.cube.position().add(look.scale(2));
            for(int i = 0 ; i < 3 ; i ++)
                serverLevel.sendParticles(ParticleTypes.EXPLOSION, center.x, center.y + i, center.z, 0, 0, 0, 0, 0);
            serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(2)).stream()
                    .filter(e -> EntityHurtEvent.shouldHurt(this.cube, e))
                    .forEach(living -> living.hurt(
                            ElementSource.createDamageSource(this.cube.damageSources().mobAttack(this.cube),
                                    new ElementSource(ElementRegistry.ELECTRO.get(), DMG_LOCATION, 1, true)),
                            (float) this.cube.getAttributeValue(Attributes.ATTACK_DAMAGE)
                    ));
        }
        if(this.target != null && this.timer <= 3) {
            this.cube.getLookControl().setLookAt(this.target, 40, 40);
            this.cube.lookAt(this.target, 40, 40);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.timer <= 16;
    }

    @Override
    public void stop() {
        this.cube.turnTo(HypostasisCubeState.EMPTY, 10);
        this.cube.clampCd = 200;
        super.stop();
    }
}
