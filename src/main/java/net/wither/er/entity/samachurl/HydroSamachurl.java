package net.wither.er.entity.samachurl;

import com.mojang.serialization.Dynamic;
import net.mcreator.er.init.ErModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 水丘丘萨满，使用基于 {@link Brain} 的声明式 AI（参考 Piglin）。
 *
 * <p>克制：
 * <ul>
 *   <li>感知器记忆附近友军（{@code #er:hilichurl}）与敌人，无敌人时沿袭友军目标；</li>
 *   <li>友军或自身血量低于 90% 时在友军处尝试施放 CallRain；</li>
 *   <li>有敌人时按距离（&lt;2 近战 / &lt;5 远离 / 否则远程 Flow）决策。</li>
 * </ul>
 *
 * @see HydroSamachurlAi
 */
public class HydroSamachurl extends Samachurl {
    /** 近战挥击的总时长（tick）。 */
    private static final int MELEE_SWING_TICKS = 20;
    /** 近战命中判定距离（格）。 */
    private static final double MELEE_HIT_RANGE = 3.0D;
    /** 近战伤害倍率：最终伤害 = 攻击力 × 该倍率。 */
    private static final float MELEE_DMG_MULTIPLIER = 2.0F;
    /** 近战冷却时长（tick，40 tick = 2 秒）。 */
    private static final int MELEE_COOLDOWN_TICKS = 40;

    /** 待近战结算的目标（非空表示正处于挥击中）。 */
    @Nullable private LivingEntity pendingMeleeTarget;
    /** 挥击剩余 tick，归零时结算伤害。 */
    private int meleeSwingTicks;
    /** 近战冷却剩余 tick，归零才允许再次挥击。 */
    private int meleeCooldownTicks;

    public HydroSamachurl(EntityType<? extends Samachurl> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Brain.@NotNull Provider<HydroSamachurl> brainProvider() {
        return Brain.provider(HydroSamachurlAi.MEMORY_TYPES, HydroSamachurlAi.SENSOR_TYPES);
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return HydroSamachurlAi.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Brain<HydroSamachurl> getBrain() {
        return (Brain<HydroSamachurl>) super.getBrain();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor accessor, @NotNull DifficultyInstance instance, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData groupData, CompoundTag tag) {
        this.setItemInHand(InteractionHand.MAIN_HAND, ErModItems.RAIN_PRAYER.get().getDefaultInstance());
        return super.finalizeSpawn(accessor, instance, spawnType, groupData, tag);
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("hydroSamachurlBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        HydroSamachurlAi.updateActivity(this);
        this.tickMeleeSwing();
        super.customServerAiStep();
    }

    // ===== 三个技能：目前均留空，仅记录当前 Action 与看向目标 =====

    /** 近战攻击：距离过近时触发。先启动挥动动画，结束后结算伤害，进入 60 tick 冷却。 */
    protected void meleeAttack(LivingEntity target) {
        // 冷却中，忽略触发
        if (this.meleeCooldownTicks > 0) {
            return;
        }
        // 已在挥击中，忽略重复触发，等待本次结算完成
        if (this.pendingMeleeTarget != null) {
            return;
        }
        this.navigation.stop();
        this.setAction(Action.MELEE);
        this.swing(InteractionHand.MAIN_HAND);
        lookAt(target);
        this.pendingMeleeTarget = target;
        this.meleeSwingTicks = MELEE_SWING_TICKS;
    }

    /**
     * 每 tick 推进近战冷却与结算：冷却递减；倒计时结束后按条件命中判定。
     *
     * <p>命中条件：目标仍存活，且其与本实体距离小于 {@value #MELEE_HIT_RANGE}，
     * 且「目标位置 - 本实体位置」的向量点乘本实体目光向量为正（即目标位于面朝方向的半空间内）；
     * 满足时对该目标造成 {@value #MELEE_DMG_MULTIPLIER} 倍攻击力伤害，并进入 {@value #MELEE_COOLDOWN_TICKS} tick 冷却。
     */
    private void tickMeleeSwing() {
        if (this.meleeCooldownTicks > 0) {
            this.meleeCooldownTicks--;
        }

        if (this.pendingMeleeTarget == null) {
            return;
        }
        if (--this.meleeSwingTicks > 0) {
            return;
        }

        LivingEntity target = this.pendingMeleeTarget;
        this.pendingMeleeTarget = null;
        this.meleeSwingTicks = 0;
        this.setAction(Action.NORMAL);

        if (!target.isAlive() || this.isRemoved()) {
            return;
        }

        Vec3 toTarget = target.position().subtract(this.position());
        boolean inRange = toTarget.lengthSqr() < MELEE_HIT_RANGE * MELEE_HIT_RANGE;
        boolean facing = toTarget.dot(this.getLookAngle()) > 0.0D;
        if (inRange && facing) {
            target.hurt(
                this.damageSources().mobAttack(this),
                (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * MELEE_DMG_MULTIPLIER)
            );
        }
        // 无论命中与否，本次挥击结束后进入冷却
        this.meleeCooldownTicks = MELEE_COOLDOWN_TICKS;
    }

    /** 治疗技能（CallRain）：在指定位置施放，用于治疗友军或自身，带冷却。 */
    public RainCloud callRain(Vec3 pos) {
        this.setAction(Action.CONJURE_CONSTANT);
        lookAt(pos);
        RainCloud cloud = new RainCloud(this.level(), pos.add(0, 4, 0), this, 0.3f);
        this.level().addFreshEntity(cloud);
        this.navigation.stop();
        return cloud;
    }

    /** 水流技能（Flow）：在目标处远程施放，带冷却。 */
    protected void flow(LivingEntity target) {
        this.setAction(Action.CONJURE);
        this.navigation.stop();
        lookAt(target);
    }

    /** 看向某个实体目标。 */
    private void lookAt(LivingEntity target) {
        lookAt(target.getX(), target.getEyeY(), target.getZ());
    }

    /** 看向某个坐标（包内可见，供引导行为在引导期间持续调用）。 */
    void lookAt(Vec3 pos) {
        lookAt(pos.x, pos.y, pos.z);
    }

    /**
     * 使身体与头部都转向目标方向：既写入 {@code LOOK_TARGET} 供 {code LookAtTargetSink} 使用，
     * 也直接旋转躯干朝向，确保「看着目标」在施法时清晰可见。
     */
    private void lookAt(double x, double y, double z) {
        this.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(new Vec3(x, y, z)));
        double dx = x - this.getX();
        double dz = z - this.getZ();
        if (dx * dx + dz * dz > 1.0E-4) {
            float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;
            float step = Mth.clamp(Mth.wrapDegrees(targetYaw - this.getYRot()), -20.0F, 20.0F);
            this.setYRot(this.getYRot() + step);
            this.yHeadRot = this.getYRot();
            this.yBodyRot = this.getYRot();
        }
    }
}