package net.wither.er.item;

import net.mcreator.er.init.ErModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.wither.er.block.ElementalFarmBlock;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

public class ElementalHoe extends HoeItem {
    private final Element.Category category;
    public ElementalHoe(Element.Category category) {
        super(Tiers.IRON, -2, -1.0F, new Item.Properties());
        this.category = category;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState state = level.getBlockState(blockpos);
        if(state.getBlock() instanceof FarmBlock){
            level.setBlock(blockpos, ErModBlocks.ELEMENTAL_FARMLAND.get().defaultBlockState().setValue(ElementalFarmBlock.ELEMENT, category), 11);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useOn(context);
    }
}
