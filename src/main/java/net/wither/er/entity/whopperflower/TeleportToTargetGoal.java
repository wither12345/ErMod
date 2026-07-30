package net.wither.er.entity.whopperflower;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class TeleportToTargetGoal extends Goal {
    private final Whopperflower whopperflower;
    private Entity target;
    private final double minDistanceSqrt ;
    private int time ;
    private boolean findPos = false;
    private int timePop = 0;

    public TeleportToTargetGoal(Whopperflower whopperflower, double minDistanceSqrt) {
        this.whopperflower = whopperflower;
        this.minDistanceSqrt = minDistanceSqrt;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if(this.whopperflower.cd > 0 || this.whopperflower.borrowCd > 0)
            return false;
        this.target = this.whopperflower.getTarget();
        return this.target != null && this.target.isAlive() && this.target.distanceToSqr(this.whopperflower) >= minDistanceSqrt && whopperflower.getAction() == Whopperflower.Action.NORMAL;
    }

    @Override
    public void start() {
        this.whopperflower.setAction(Whopperflower.Action.CLOSING);
        this.time = 0;
        this.findPos = false;
        this.timePop = 0;
        this.whopperflower.cd = 40;
    }

    @Override
    public boolean canContinueToUse() {
        return this.timePop <= 10;
    }

    @Override
    public void stop() {
        this.whopperflower.borrowCd = 60;
        this.whopperflower.trySpawnFruit();
        this.whopperflower.setAction(Whopperflower.Action.NORMAL);
    }

    @Override
    public void tick() {
        if(this.time >= 6) {
            if(findPos) {
                this.timePop ++ ;
                if(timePop == 2)
                    this.whopperflower.setAction(Whopperflower.Action.UP);
                if(timePop > 5)
                    this.whopperflower.setAction(Whopperflower.Action.OPENING);
            }
            else {
                for (int i = 0; i < 10; i++) {
                    if (this.whopperflower.borrowTowards(target)) {
                        findPos = true;
                        break;
                    }
                }
            }
        }
        else {
            this.time ++ ;
            if(this.time == 2)
                this.whopperflower.setAction(Whopperflower.Action.DOWN);
        }
    }
}
