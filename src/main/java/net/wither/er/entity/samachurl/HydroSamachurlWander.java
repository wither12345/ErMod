package net.wither.er.entity.samachurl;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 闲置游荡行为：每次随机挑选一个方向并朝该方向前进一小段，随后重新随机。
 *
 * <p>游荡被「引导状态」门控——当 {@code HydroSamachurlAi.isChanneling(mob)} 为真时（正在施放 CallRain），
 * 该行为不会启动，也不会写入 {@code WALK_TARGET}，从而让丘丘萨满在施法期间保持站立不动。
 */
public class HydroSamachurlWander extends Behavior<HydroSamachurl> {
    /** 单次随机目标推进半径（格）。 */
    private static final int RANGE = 6;
    /** 单次移动预期的持续时长范围（tick）。 */
    private static final int MIN_DURATION = 40;
    private static final int MAX_DURATION = 100;

    public HydroSamachurlWander() {
        super(Map.of(), MIN_DURATION, MAX_DURATION);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull HydroSamachurl mob) {
        return !HydroSamachurlAi.isChanneling(mob);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull HydroSamachurl mob, long gameTime) {
        // 游荡途中若开始引导技能，立即停下
        return !HydroSamachurlAi.isChanneling(mob);
    }

    @Override
    protected void start(@NotNull ServerLevel level, HydroSamachurl mob, long gameTime) {
        int range = mob.getRandom().nextInt(RANGE * 2) - RANGE;
        int range1 = mob.getRandom().nextInt(RANGE * 2) - RANGE;
        BlockPos target = new BlockPos(mob.blockPosition().getX() + range, mob.blockPosition().getY(), mob.blockPosition().getZ() + range1);
        mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(target, 0.6F, 2));
    }
}