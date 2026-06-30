package net.mcreator.er.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class LotusHead_Growth_SiteProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		return (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.AIR && !((world.getBlockState(BlockPos.containing(x, y - 5, z))).getBlock() == Blocks.AIR);
	}
}