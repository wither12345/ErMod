package net.wither.er.block;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;

public class DestroyBlocks {
    private static final Object2IntMap<BlockPos> destroyingBlocks = new Object2IntOpenHashMap<>();

    public static void destroy(ServerLevel serverLevel, BlockPos pos, Entity entity, int progress){
        if(destroyingBlocks.containsKey(pos)){
            int val = destroyingBlocks.getInt(pos);
            progress = progress + val;
            if(progress >= 10)
                destroyingBlocks.remove(pos, val);
        }
        if(progress < 10){
            destroyingBlocks.put(pos, progress);
            serverLevel.levelEvent(2001, pos, Block.getId(serverLevel.getBlockState(pos)));
        }
        else
            serverLevel.destroyBlock(pos, true, entity);
        serverLevel.destroyBlockProgress(pos.hashCode(), pos, progress);
    }
}
