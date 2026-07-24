package net.wither.er.entity.whopperflower;

import net.wither.er.init.ShieldRegistry;

public class PyroConsumeFruitGoal extends ConsumeFruitGoal{
    private int fireTime;
    private int fireCount;
    public PyroConsumeFruitGoal(Whopperflower whopperflower) {
        super(whopperflower, ShieldRegistry.PYRO_WHOPPERFLOWER.get());
    }

    @Override
    protected void afterConsume() {
        this.fireCount = 8;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() || ((this.fireCount > 0 || this.fireTime > 0) && this.target != null && this.target.isAlive());
    }

    @Override
    public void start() {
        super.start();
        this.fireTime = 0;
        this.fireCount = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.fireTime > 0){
            this.whopperflower.getLookControl().setLookAt(target, 10, 10);
            if(this.fireTime == 10) {
                this.whopperflower.setAction(Whopperflower.Action.FIRE);
            }
            if(this.fireTime == 6){
                PyroWhopperflowerBall ball = new PyroWhopperflowerBall(whopperflower, target);
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
