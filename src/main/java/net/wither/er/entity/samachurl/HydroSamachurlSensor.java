package net.wither.er.entity.samachurl;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 丘丘萨满的感知器，负责在每轮扫描时刷新「附近友军」与「所见敌人」的记忆。
 *
 * <p>逻辑：
 * <ol>
 *   <li>在周围 {@value HydroSamachurlAi#SENSOR_RANGE} 格内收集友军（命中 {@code #er:hilichurl} 标签）；
 *       非友军且属于 Player / IronGolem / Monster 的视为潜在敌人。</li>
 *   <li>若存在敌人则把最近的敌人写入 {@code ATTACK_TARGET}。</li>
 *   <li>若没有敌人，则顺次尝试沿袭最近一名友军记忆里的 {@code ATTACK_TARGET}（友军看到谁，我们就打谁）；
 *       仍找不到则清空自身目标，避免原地返回战斗状态。</li>
 * </ol>
 *
 * @see Sensor
 */
public class HydroSamachurlSensor extends Sensor<HydroSamachurl> {

    @Override
    protected void doTick(ServerLevel level, HydroSamachurl mob) {
        List<LivingEntity> allies = new ArrayList<>();
        List<LivingEntity> enemies = new ArrayList<>();

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(HydroSamachurlAi.SENSOR_RANGE))) {
            if (entity == mob || !entity.isAlive()) {
                continue;
            }
            if (entity.getType().is(HydroSamachurlAi.HILICHURL_TAG)) {
                allies.add(entity);
            } else if (isEnemy(entity) && TargetingConditions.forCombat().test(mob, entity)) {
                enemies.add(entity);
            }
        }

        mob.getBrain().setMemory(HydroSamachurlAi.NEARBY_ALLIES, allies);

        double rangeSq = HydroSamachurlAi.SENSOR_RANGE * HydroSamachurlAi.SENSOR_RANGE;
        LivingEntity target = findNearest(enemies, mob, rangeSq);
        if (target != null) {
            mob.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
            return;
        }

        // 没有自己的敌人时，尝试沿用友军的攻击目标
        for (LivingEntity ally : allies) {
            if (ally instanceof Mob mobAlly) {
                Optional<LivingEntity> adopted = mobAlly.getBrain().getMemoryInternal(MemoryModuleType.ATTACK_TARGET);
                if (adopted != null && adopted.isPresent() && adopted.get().isAlive()) {
                    mob.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, adopted.get());
                    return;
                }
            }
        }

        mob.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
    }

    private static boolean isEnemy(LivingEntity entity) {
        return (entity instanceof Player player && !player.isCreative() && !player.isSpectator()) || entity instanceof IronGolem;
    }



    /**
     * 在给定集合中找出距离 {@code mob} 最近且未超出 {@code rangeSq} 的实体，找不到则返回 {@code null}。
     */
    private static LivingEntity findNearest(List<LivingEntity> candidates, HydroSamachurl mob, double rangeSq) {
        LivingEntity nearest = null;
        double best = rangeSq + 1;
        for (LivingEntity entity : candidates) {
            double dist = mob.distanceToSqr(entity);
            if (dist <= rangeSq && dist < best) {
                best = dist;
                nearest = entity;
            }
        }
        return nearest;
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return Set.of(HydroSamachurlAi.NEARBY_ALLIES, MemoryModuleType.ATTACK_TARGET);
    }
}