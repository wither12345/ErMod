package net.wither.er.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.wither.er.elements.Element;

import javax.annotation.Nullable;
import java.util.Optional;

public interface IBlockBehavior {
    boolean er$isBelow();
    Optional<ReactionBehavior> er$getReactionBehavior();
    void er$setReactionBehavior(@Nullable ReactionBehavior behavior, boolean below);

    @FunctionalInterface
    interface ReactionBehavior{
        void reacted(Level level, BlockPos blockPos, @Nullable Element element, @Nullable Entity applier, boolean melee);
    }
}
