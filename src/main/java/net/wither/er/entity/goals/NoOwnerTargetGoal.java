package net.wither.er.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class NoOwnerTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    OwnableEntity ownableEntity;
    public NoOwnerTargetGoal(Mob mob, Class<T> target, boolean flag) {
        super(mob, target, flag);
        if(mob instanceof OwnableEntity ownable)
            this.ownableEntity = ownable;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && ownableEntity != null && ownableEntity.getOwner() == null;
    }
}
