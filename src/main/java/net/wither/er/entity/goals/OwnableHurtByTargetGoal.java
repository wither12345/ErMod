package net.wither.er.entity.goals;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OwnableHurtByTargetGoal extends HurtByTargetGoal {
    public OwnableHurtByTargetGoal(PathfinderMob mob, Class<?>... classes) {
        super(mob, classes);
    }

    @Override
    protected boolean canAttack(@Nullable LivingEntity living, @NotNull TargetingConditions conditions) {
        return super.canAttack(living, conditions) && EntityHurtEvent.shouldHurt(living, mob);
    }
}
