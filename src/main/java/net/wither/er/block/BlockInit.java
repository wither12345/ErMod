package net.wither.er.block;

import net.mcreator.er.init.ErModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.wither.er.block.entity.BurningDirtEntity;
import net.wither.er.elements.Element;
import net.wither.er.item.weapons.Claymore;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    @SubscribeEvent
    public static void setUp(FMLCommonSetupEvent event){
        putReactionBehavior(Blocks.GRASS_BLOCK, BlockInit::grassBlock, true);
        putReactionBehavior(Blocks.STONE, BlockInit::rock, false);
        putReactionBehavior(Blocks.GRANITE, BlockInit::rock, false);
        putReactionBehavior(Blocks.DIORITE, BlockInit::rock, false);
        putReactionBehavior(Blocks.ANDESITE, BlockInit::rock, false);
        putReactionBehavior(Blocks.TUFF, BlockInit::rock, false);
        putReactionBehavior(Blocks.AIR, BlockInit::triggerBelow, false);
        putReactionBehavior(Blocks.CAVE_AIR, BlockInit::triggerBelow, false);
        putReactionBehavior(Blocks.VOID_AIR, BlockInit::triggerBelow, false);
        putReactionBehavior(Blocks.WATER, BlockInit::water, true);
        putReactionBehavior(Blocks.ICE, BlockInit::ice, true);
        putReactionBehavior(Blocks.FROSTED_ICE, BlockInit::ice, true);
    }

    private static void putReactionBehavior(@NotNull Block block, @NotNull IBlockBehavior.ReactionBehavior behavior, boolean below){
        if(block instanceof IBlockBehavior blockBehavior)
            blockBehavior.er$setReactionBehavior(behavior, below);
    }

    private static void water(Level level, BlockPos blockPos, @Nullable Element element, @Nullable Entity applier, boolean melee){
        if(element != null && level.getBlockState(blockPos).getFluidState().isSource() && element.getCategory() == Element.Category.CRYO)
            level.setBlock(blockPos, Blocks.FROSTED_ICE.defaultBlockState(), 3);
    }

    private static void ice(Level level, BlockPos blockPos, @Nullable Element element, @Nullable Entity applier, boolean melee){
        if(element != null && element.getCategory() == Element.Category.PYRO)
            level.setBlock(blockPos, Blocks.WATER.defaultBlockState(), 3);
    }

    private static void triggerBelow(Level level, BlockPos blockPos, @Nullable Element element, @Nullable Entity applier, boolean melee){
        BlockPos below = blockPos.below();
        if(level.getBlockState(below).getBlock() instanceof IBlockBehavior blockBehavior && blockBehavior.er$isBelow())
            blockBehavior.er$getReactionBehavior().ifPresent(a -> a.reacted(level, below, element, applier, melee));
    }

    private static void grassBlock(Level level, BlockPos blockPos, @Nullable Element element, @Nullable Entity applier, boolean melee){
        if(element != null && element.getCategory() == Element.Category.PYRO) {
            level.setBlock(blockPos, ErModBlocks.BURNING_DIRT.get().defaultBlockState(), 3);
            if (level.getBlockEntity(blockPos) instanceof BurningDirtEntity entity)
                entity.setOwner(applier);
        }
    }

    private static void rock(Level level, BlockPos blockPos, @Nullable Element element, @Nullable Entity applier, boolean melee){
        if(melee){
            int breakSpeed = 0;
            if (applier instanceof LivingEntity living && living.getMainHandItem().getItem() instanceof Claymore)
                breakSpeed += 3;
            if (element != null && element.getCategory() == Element.Category.GEO)
                breakSpeed += 3;
            if (breakSpeed > 0 && level instanceof ServerLevel serverLevel)
                DestroyBlocks.destroy(serverLevel, blockPos, applier, breakSpeed);
        }
    }
}
