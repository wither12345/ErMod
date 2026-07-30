package net.wither.er.entity.whopperflower;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;

import java.util.EnumSet;

public class HidingGoal extends Goal {
    private final Whopperflower whopperflower;
    private int time ;

    public HidingGoal(Whopperflower whopperflower) {
        this.whopperflower = whopperflower;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        return !whopperflower.isDisguise() &&
                whopperflower.canDisguise &&
                whopperflower.getTarget() == null &&
                whopperflower.getAction() == Whopperflower.Action.NORMAL &&
                (whopperflower.level().getBlockState(whopperflower.getOnPos()).is(Blocks.GRASS_BLOCK) ||
                        whopperflower.level().getBlockState(whopperflower.getOnPos()).is(BlockTags.DIRT));
    }

    @Override
    public void start() {
        this.whopperflower.setAction(Whopperflower.Action.CLOSING);
        this.time = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.time < 8;
    }

    @Override
    public void stop() {
        this.whopperflower.disguise();
    }

    @Override
    public void tick() {
        this.time ++ ;
        if(this.time == 2)
            this.whopperflower.setAction(Whopperflower.Action.DOWN);
    }
}
