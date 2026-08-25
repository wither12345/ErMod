package net.wither.er.item;

import net.mcreator.er.init.ErModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.wither.er.block.LinkMechanismBase;
import org.jetbrains.annotations.NotNull;

public class LinkMechanismItem extends BlockItem {
    public LinkMechanismItem() {
        super(ErModBlocks.LINK_MECHANISM_BASE.get(), new Properties());
    }

    @Override
    protected boolean placeBlock(@NotNull BlockPlaceContext context, @NotNull BlockState blockState) {
        Direction direction = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();
        if(!check4Pos(context.getLevel(), pos, direction))
            return false;
        place4Block(context.getLevel(), pos, direction);
        return super.placeBlock(context, blockState);
    }

    private static boolean check4Pos(Level level, BlockPos pos, Direction direction){
        for(int i = 0; i < 4; i ++){
            if(!level.getBlockState(pos).canBeReplaced())
                return false;
            pos = pos.relative(direction);
            direction = LinkMechanismBase.next(direction);
        }
        return true;
    }

    private static void place4Block(Level level, BlockPos pos, Direction direction){
        Direction pd = direction;
        for(int i = 0; i < 3; i ++){
            pos = pos.relative(pd);
            pd = LinkMechanismBase.next(pd);
            level.setBlock(pos,
                    ErModBlocks.LINK_MECHANISM_BASE.get().defaultBlockState()
                            .setValue(HorizontalDirectionalBlock.FACING, pd)
                            .setValue(BlockStateProperties.EXTENDED, i != 1)
                            .setValue(BlockStateProperties.PERSISTENT, false)
                            .setValue(BlockStateProperties.WATERLOGGED, level.getFluidState(pos).is(Fluids.WATER)),
                    11);
        }
    }

}
