package net.wither.er.entity.whopperflower;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class CrystallineSpikesGoal extends Goal {
    private int time;
    protected final CryoWhopperflower whopperflower;
    protected Entity target;
    private Vec3 dVec ;
    private Vec3 nowVec ;
    public CrystallineSpikesGoal(CryoWhopperflower whopperflower) {
        this.whopperflower = whopperflower;
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(this.whopperflower.cd > 0 ||
                this.whopperflower.getAction() != Whopperflower.Action.NORMAL ||
                this.whopperflower.spikeCd > 0
        ) return false;
        this.target = this.whopperflower.getTarget();
        return this.target != null && this.target.isAlive() && this.target.distanceToSqr(this.whopperflower) < 36;
    }

    @Override
    public boolean canContinueToUse() {
        return this.time ++ < 10 ;
    }

    @Override
    public void stop() {
        this.whopperflower.cd = 20;
        this.whopperflower.spikeCd = 40;
        this.whopperflower.setAction(Whopperflower.Action.NORMAL);
    }

    @Override
    public void start() {
        this.whopperflower.lookAt(this.target, 180, 90);
        this.nowVec = this.whopperflower.position();
        this.dVec = this.target.position().subtract(nowVec).normalize();
        this.nowVec = this.nowVec.subtract(dVec.scale(-3));
        this.whopperflower.setAction(Whopperflower.Action.LOWER_HEAD);
        this.time = 0;
    }

    @Override
    public void tick() {
        if(this.time >= 3 && this.time <= 8){
            this.nowVec = this.nowVec.add(this.dVec);
            this.whopperflower.level().addFreshEntity(new CryoSpike(nowVec, this.whopperflower));
        }
    }

}
