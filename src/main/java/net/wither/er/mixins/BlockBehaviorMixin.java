package net.wither.er.mixins;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.wither.er.block.IBlockBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(BlockBehaviour.class)
public class BlockBehaviorMixin implements IBlockBehavior{
    @Unique
    private boolean er$below = false;
    @Unique @Nullable
    private IBlockBehavior.ReactionBehavior er$behavior = null;

    @Override
    public boolean er$isBelow() {
        return er$below;
    }

    @Override
    public Optional<IBlockBehavior.ReactionBehavior> er$getReactionBehavior() {
        return Optional.ofNullable(er$behavior);
    }

    @Override
    public void er$setReactionBehavior(@Nullable IBlockBehavior.ReactionBehavior behavior, boolean below) {
        this.er$behavior = behavior;
        this.er$below = below;
    }
}
