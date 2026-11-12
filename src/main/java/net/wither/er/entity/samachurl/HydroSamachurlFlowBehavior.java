package net.wither.er.entity.samachurl;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * Flow 远程技能：远距离（≥ 5 格）施放，持续引导。
 *
 * <ol>
 *   <li>启动后先经历 1 秒（20 tick）前摇：萨满站立不动、持续注视目标；</li>
 *   <li>前摇结束后每 2 秒（40 tick）在目标脚下贴近地面处召唤一个 {@link FlowEntity}；</li>
 *   <li>引导持续 10 秒后结束，进入 Flow 冷却。引导期间受到伤害会立刻打断。</li>
 * </ol>
 *
 * @see FlowEntity
 */
public class HydroSamachurlFlowBehavior extends Behavior<HydroSamachurl> {

    /** 引导总时长（tick，10 秒 = 200 tick）。 */
    private static final int DURATION_TICKS = 80;
    /** 前摇时长（tick，1 秒 = 20 tick）。 */
    private static final int WINDUP_TICKS = 30;
    /** 每次召唤 FlowEntity 的间隔（tick，2 秒 = 40 tick）。 */
    private static final int SUMMON_INTERVAL = 20;

    private LivingEntity target;
    private long startTick;
    private long nextSummonTick;
    private float lastHealth;

    public HydroSamachurlFlowBehavior() {
        super(Map.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), DURATION_TICKS, DURATION_TICKS);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, HydroSamachurl mob) {
        if (mob.getBrain().hasMemoryValue(HydroSamachurlAi.FLOW_COOLDOWN)) {
            return false;
        }
        if (HydroSamachurlAi.isChanneling(mob)) {
            return false;
        }
        Optional<LivingEntity> oTarget = mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (oTarget.isEmpty() || !oTarget.get().isAlive()) {
            return false;
        }
        double dist = mob.distanceTo(oTarget.get());
        return dist >= HydroSamachurlAi.RETREAT_RANGE;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull HydroSamachurl mob, long gameTime) {
        // 目标死亡 → 停止
        if (this.target == null || !this.target.isAlive()) {
            return false;
        }
        // 引导期间受到伤害 → 立即打断
        boolean damaged = mob.getHealth() < this.lastHealth;
        if (mob.getHealth() > this.lastHealth) {
            this.lastHealth = mob.getHealth();
        }
        return !damaged;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull HydroSamachurl mob, long gameTime) {
        this.target = mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
        this.startTick = gameTime;
        this.nextSummonTick = gameTime + WINDUP_TICKS;
        this.lastHealth = mob.getHealth();
        mob.getBrain().setMemory(HydroSamachurlAi.CHANNELING, Unit.INSTANCE);
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.setAction(Samachurl.Action.CONJURE);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull HydroSamachurl mob, long gameTime) {
        // 全程持续注视目标（前摇 + 召唤阶段）
        mob.lookAt(this.target.position());

        long elapsed = gameTime - this.startTick;
        if (elapsed < WINDUP_TICKS) {
            return; // 前摇中，还不召唤
        }

        if (gameTime >= this.nextSummonTick) {
            // 在目标脚下贴近地面处召唤 FlowEntity
            FlowEntity flow = new FlowEntity(level, findGroundPosition(level, this.target), mob);
            level.addFreshEntity(flow);
            this.nextSummonTick = gameTime + SUMMON_INTERVAL;
        }
    }

    /**
     * 查找目标脚下贴近地面的坐标。
     * @param level 世界
     * @param target 目标实体
     * @return 目标脚下贴近地面的坐标
     */
    private static Vec3 findGroundPosition(ServerLevel level, LivingEntity target) {
        // 从目标脚部略上方开始，避免起点在方块内部导致射线直接失败
        Vec3 start = target.position().add(0, 0.1, 0);
        // 向下最多检测 16 格（可根据需要调整）
        Vec3 end = start.subtract(0, 16, 0);

        BlockHitResult hit = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,   // 只检测有碰撞的方块（不含空气/流体）
                ClipContext.Fluid.NONE,       // 不检测流体
                target                        // 忽略目标自身
        ));

        if (hit.getType() == HitResult.Type.BLOCK) {
            // hit.getLocation() 就是地面方块顶面的精确坐标（脚部位置）
            return hit.getLocation();
        }
        // 找不到地面（虚空、超高空等），回退到原位置
        return target.position();
    }

    @Override
    protected void stop(@NotNull ServerLevel level, HydroSamachurl mob, long gameTime) {
        mob.getBrain().eraseMemory(HydroSamachurlAi.CHANNELING);
        mob.getBrain().setMemoryWithExpiry(HydroSamachurlAi.FLOW_COOLDOWN, Unit.INSTANCE, HydroSamachurlAi.FLOW_COOLDOWN_TICKS);
        mob.setAction(Samachurl.Action.NORMAL);
        this.target = null;
    }
}