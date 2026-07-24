package net.wither.er.item;

import net.mcreator.er.init.ErModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.wither.er.block.entity.WhopperflowerCropEntity;
import org.jetbrains.annotations.NotNull;

public class WhopperflowerSeedItem extends BlockItem {
    public WhopperflowerSeedItem() {
        super(ErModBlocks.WHOPPERFLOWER_CROP.get(),  new Properties());
    }

    @Override
    protected boolean placeBlock(@NotNull BlockPlaceContext context, @NotNull BlockState blockState) {
        boolean ret = super.placeBlock(context, blockState);
        if(context.getLevel().getBlockEntity(context.getClickedPos()) instanceof WhopperflowerCropEntity whopperflowerCropEntity){
            whopperflowerCropEntity.setOwner(context.getPlayer());
        }
        return ret;
    }
}
