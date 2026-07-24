package net.wither.er.entity.whopperflower;

import net.mcreator.er.ErMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HomingOrbGoal extends Goal {
    private int fireTime;
    private int fireCount;
    protected final PyroWhopperflower whopperflower;
    protected Entity target;
    public HomingOrbGoal(PyroWhopperflower whopperflower) {
        this.whopperflower = whopperflower;
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(this.whopperflower.cd > 0 ||
                this.whopperflower.homingOrbCd > 0 ||
                this.whopperflower.getAction() != Whopperflower.Action.NORMAL
        ) return false;
        this.target = this.whopperflower.getTarget();
        return this.target != null && this.target.isAlive() && this.target.distanceToSqr(this.whopperflower) > 9;
    }

    @Override
    public boolean canContinueToUse() {
        ErMod.LOGGER.info(this.fireCount + " " + this.fireTime + "   " + this.target);
        return ((this.fireCount > 0 || this.fireTime > 0) && this.target != null && this.target.isAlive());
    }

    @Override
    public void stop() {
        this.whopperflower.homingOrbCd = 160;
        this.whopperflower.cd = 20;
        this.whopperflower.setAction(Whopperflower.Action.NORMAL);
    }

    @Override
    public void start() {
        this.fireTime = 0;
        this.fireCount = 3;
    }

    @Override
    public void tick() {
        if(this.fireTime > 0){
            this.whopperflower.getLookControl().setLookAt(target, 10, 10);
            if(this.fireTime == 10) {
                this.whopperflower.setAction(Whopperflower.Action.FIRE);
            }
            if(this.fireTime == 6){
                PyroHomingOrb ball = new PyroHomingOrb(whopperflower, target);
                this.whopperflower.level().addFreshEntity(ball);
            }
            if(this.fireTime == 2)
                this.whopperflower.setAction(Whopperflower.Action.NORMAL);
            this.fireTime -- ;
        }
        else if(this.fireCount > 0){
            this.fireCount -- ;
            this.fireTime = 10;
        }
    }
}
