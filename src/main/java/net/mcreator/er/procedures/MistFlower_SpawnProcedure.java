package net.mcreator.er.procedures;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class MistFlower_SpawnProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		return (world instanceof Level _lvl ? _lvl.dimension() : (world instanceof WorldGenLevel _wgl ? _wgl.getLevel().dimension() : Level.OVERWORLD)) == Level.OVERWORLD && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y, z));
	}
}