package net.wither.er.entity.whopperflower;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class LookAtTargetGoal extends Goal {
    private final Whopperflower whopperflower;
    private Entity target;

    public LookAtTargetGoal(Whopperflower whopperflower) {
        this.whopperflower = whopperflower;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.target = this.whopperflower.getTarget();
        return this.target != null && this.target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.target.isAlive() && this.target == this.whopperflower.getTarget();
    }

    @Override
    public void tick() {
        this.whopperflower.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
    }
}
