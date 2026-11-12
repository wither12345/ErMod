package net.wither.er.entity.samachurl;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * 战斗行为：在有攻击目标期间，依据与目标的距离执行对应分支。
 *
 * <ol>
 *   <li>{@code d < 2}：直接近战（{@code meleeAttack}，无冷却）；</li>
 *   <li>{@code 2 <= d < 5}：尝试远离目标（设置背离方向的行进目标 {@code WALK_TARGET}）；</li>
 *   <li>{@code d >= 5}：在目标位置施放 Flow 技能（有冷却，冷却期间仅原地待命）。</li>
 * </ol>
 *
 * <p>为使分支互斥，三个分支统一收敛在此一个常驻行为内，每 tick 按其当前距离重新决策。
 * 目标消失或死亡时行为自动停止，并清除可能残留的行进目标。
 *
 * @see HydroSamachurl#meleeAttack(LivingEntity)
 * @see HydroSamachurl#flow(LivingEntity)
 */
public class HydroSamachurlFightBehavior extends Behavior<HydroSamachurl> {

    /** 常驻时长（tick），足够长以免中途超时打断分支决策。 */
    private static final int STAY_DURATION = 1_000_000;

    public HydroSamachurlFightBehavior() {
        super(Map.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), STAY_DURATION, STAY_DURATION);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull HydroSamachurl mob) {
        return !HydroSamachurlAi.isChanneling(mob);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, HydroSamachurl mob, long gameTime) {
        Optional<LivingEntity> target = mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        return target.filter(LivingEntity::isAlive).isPresent();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull HydroSamachurl mob, long gameTime) {
        tickCombat(level, mob);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull HydroSamachurl mob, long gameTime) {
        tickCombat(level, mob);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, HydroSamachurl mob, long gameTime) {
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    private void tickCombat(ServerLevel level, HydroSamachurl mob) {
        // 任何引导行为（Flow 前摇 / CallRain 引导）期间，站桩不动，让引导行为独自处理
        if (HydroSamachurlAi.isChanneling(mob)) {
            mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            return;
        }

        Optional<LivingEntity> oTarget = mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (oTarget.isEmpty() || !oTarget.get().isAlive()) {
            return;
        }
        LivingEntity target = oTarget.get();
        double dist = mob.distanceTo(target);

        if (dist < HydroSamachurlAi.MELEE_RANGE) {
            mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            mob.meleeAttack(target);
        } else if (dist < HydroSamachurlAi.RETREAT_RANGE) {
            Vec3 away = mob.position().subtract(target.position().subtract(mob.position()).normalize().scale(0.5));
            mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(away, 1.0F, 2));
        } else {
            // 远距离：让高优先级的 FlowBehavior 接管，此处清除 WALK_TARGET 站桩等待
            mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        }
    }
}