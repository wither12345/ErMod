package net.wither.er.entity.whopperflower;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.shield.ErShield;
import net.wither.er.shield.ShieldStack;

import java.util.EnumSet;

public abstract class ConsumeFruitGoal extends Goal {
    protected final Whopperflower whopperflower;
    private final ErEntityInterface erEntityInterface;
    protected Entity target;
    private final ErShield shield;
    protected int time ;
    private int stunTime;

    public ConsumeFruitGoal(Whopperflower whopperflower, ErShield shield) {
        this.whopperflower = whopperflower;
        this.erEntityInterface = (ErEntityInterface)whopperflower;
        this.shield = shield;
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(this.whopperflower.cd > 0 ||
                this.whopperflower.consumeFruitCd > 0 ||
                this.whopperflower.getAction() != Whopperflower.Action.NORMAL ||
                this.whopperflower.getFruitCount() <= 0) return false;
        this.target = this.whopperflower.getTarget();
        return this.target != null && this.target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.time < 120 && this.stunTime < 30;
    }

    @Override
    public void stop() {
        this.whopperflower.cd = 40;
        this.whopperflower.consumeFruitCd = 240;
        this.whopperflower.setAction(Whopperflower.Action.NORMAL);
    }

    @Override
    public void start() {
        this.time = 0;
        this.stunTime = 0;
        this.whopperflower.setAction(Whopperflower.Action.CONSUMING);
    }

    protected abstract void afterConsume();

    @Override
    public void tick() {
        if(this.whopperflower.getAction() == Whopperflower.Action.STUN){
            if(stunTime == 0) this.whopperflower.consumeFruit();
            this.stunTime ++ ;
        }
        else {
            this.time++;
            if (this.time == 4) {
                this.whopperflower.setAction(Whopperflower.Action.SHIELD);
                erEntityInterface.er$addShield(new ShieldStack(shield, 5, 200));
            }
            else if(this.time == 105){
                this.whopperflower.consumeFruit();
                afterConsume();
            }
        }
    }
}