package net.wither.er.entity.whopperflower;

import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.wither.er.shield.ShieldRegistry;

public class CryoConsumeFruitGoal extends ConsumeFruitGoal{
    private int fireTime;
    private int fireCount;
    public CryoConsumeFruitGoal(Whopperflower whopperflower) {
        super(whopperflower, ShieldRegistry.CRYO_WHOPPERFLOWER.get());
    }

    @Override
    protected void afterConsume() {
        this.fireCount = 12;
        this.whopperflower.setAction(Whopperflower.Action.FIRE_CONSTANT);
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
        this.whopperflower.getLookControl().setLookAt(target, 10, 10);
        if(this.fireTime > 0){
            this.fireTime -- ;
            if(this.fireTime == 0){
                CryoWhopperflowerProjectile projectile = new CryoWhopperflowerProjectile(this.whopperflower);
                Vec3 dv = this.target.position().subtract(this.whopperflower.position()).normalize().scale(0.5);
                this.whopperflower.level().addFreshEntity(projectile);
                projectile.move(MoverType.SELF, dv);
                projectile.shoot(dv.x, dv.y, dv.z, 1, 0);
            }
        }
        else if(this.fireCount > 0){
            this.fireCount -- ;
            this.fireTime = 2;
        }
    }
}
