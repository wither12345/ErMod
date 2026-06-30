package net.wither.er.entity.slimes;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.wither.er.elements.Element;
import net.wither.er.init.ElementRegistry;
import org.jetbrains.annotations.NotNull;

public class HydroSlime extends ElementalSlime{
    public HydroSlime(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    Element getElement() {
        return ElementRegistry.HYDRO.get();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ElementalSlimeAttackGoal(this));
        this.goalSelector.addGoal(3, new ElementalSlimeRandomDirectionGoal(this, 1));
        this.goalSelector.addGoal(4, new ElementalSlimeKeepOnJumpingGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (p_352812_) -> Math.abs(p_352812_.getY() - this.getY()) <= 4.0));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public boolean canStandOnFluid(@NotNull FluidState state) {
        return state.is(FluidTags.WATER) || super.canStandOnFluid(state) ;
    }

    @Override
    boolean isTiny() {
        return true;
    }
}
