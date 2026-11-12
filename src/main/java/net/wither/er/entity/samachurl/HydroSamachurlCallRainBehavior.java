package net.wither.er.entity.samachurl;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 治疗行为（持续引导版）：当自身或友军血量低于 {@code 90%} 时启动，引导持续 10 秒。
 *
 * <ol>
 *   <li>启动瞬间在目标位置（优先最近低血友军，其次自身）施放 CallRain，并锁定该位置为引导中心；</li>
 *   <li>引导期间每秒对中心半径 4 格内的实体做一次结算：
 *       <ul>
 *         <li>命中 {@code #er:hilichurl} 标签的友军获得治疗；</li>
 *         <li>所有实体（友军与敌军）都被附着水元素（{@code new ElementSource(ElementRegistry.HYDRO.get(), null, 2, true, true)}）。</li>
 *       </ul></li>
 *   <li>引导结束后进入 5 秒冷却。</li>
 * </ol>
 *
 * @see HydroSamachurl#callRain(net.minecraft.world.phys.Vec3)
 */
public class HydroSamachurlCallRainBehavior extends Behavior<HydroSamachurl> {
    /** 引导总时长（tick，10 秒 = 200 tick）。 */
    private static final int DURATION_TICKS = 200;
    /** 冷却时长（tick，5 秒 = 100 tick）。 */
    private static final int COOLDOWN_TICKS = 100;
    /** 引导期间记录的血量高水位，用于检测受到伤害而打断引导。 */
    private float lastHealth;
    /** 引导中心（启动时锁定，供引导期间持续注视）。 */
    @Nullable private Vec3 castCenter;
    @Nullable private RainCloud cloud;

    public HydroSamachurlCallRainBehavior() {
        super(Map.of(), DURATION_TICKS, DURATION_TICKS);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, HydroSamachurl mob) {
        return !mob.getBrain().hasMemoryValue(HydroSamachurlAi.CALL_RAIN_COOLDOWN)
            && !HydroSamachurlAi.isChanneling(mob)
            && needsHealing(mob);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, HydroSamachurl mob, long gameTime) {
        // 云已消失/提前结束 → 立即停止引导
        if (this.cloud == null || !this.cloud.isAlive()) {
            return false;
        }
        // 引导期间受到伤害 → 立即打断，其余情况保持持续完整引导时长
        boolean damaged = mob.getHealth() < this.lastHealth;
        if (mob.getHealth() > this.lastHealth) {
            this.lastHealth = mob.getHealth(); // 高水位：只有血量下降才视为受伤
        }
        return !damaged;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull HydroSamachurl mob, long gameTime) {
        LivingEntity ally = pickLowHpAlly(mob);
        Vec3 center = ally != null ? ally.getEyePosition() : mob.getEyePosition();
        this.castCenter = center;
        this.lastHealth = mob.getHealth();
        // 标记引导中，配合游荡/后撤门控实现“施法时站立不动”
        mob.getBrain().setMemory(HydroSamachurlAi.CHANNELING, Unit.INSTANCE);
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.cloud = mob.callRain(center);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull HydroSamachurl mob, long gameTime) {
        // 施法期间持续注视施法目标点
        if (this.castCenter != null) {
            mob.lookAt(this.castCenter);
        }
    }

    @Override
    protected void stop(@NotNull ServerLevel level, HydroSamachurl mob, long gameTime) {
        this.castCenter = null;
        mob.getBrain().eraseMemory(HydroSamachurlAi.CHANNELING);
        mob.getBrain().setMemoryWithExpiry(HydroSamachurlAi.CALL_RAIN_COOLDOWN, Unit.INSTANCE, COOLDOWN_TICKS);
        mob.setAction(Samachurl.Action.NORMAL);
        if (this.cloud != null) {
            this.cloud.remove(Entity.RemovalReason.DISCARDED);
            this.cloud = null;
        }
    }

    /** 是否存在需要治疗的对象（自身或某名友军血量低于 90%）。 */
    private static boolean needsHealing(HydroSamachurl mob) {
        return mob.getHealth() <= mob.getMaxHealth() * HydroSamachurlAi.LOW_HEALTH_RATIO
            || pickLowHpAlly(mob) != null;
    }

    /** 返回最近一名血量低于 90% 的友军，没有则返回 {@code null}。 */
    private static LivingEntity pickLowHpAlly(HydroSamachurl mob) {
        LivingEntity target = null;
        double best = Double.MAX_VALUE;
        for (LivingEntity ally : mob.getBrain().getMemory(HydroSamachurlAi.NEARBY_ALLIES).orElse(List.of())) {
            if (ally.isAlive() && ally.getHealth() <= ally.getMaxHealth() * HydroSamachurlAi.LOW_HEALTH_RATIO) {
                double dist = mob.distanceToSqr(ally);
                if (dist < best) {
                    best = dist;
                    target = ally;
                }
            }
        }
        return target;
    }
}