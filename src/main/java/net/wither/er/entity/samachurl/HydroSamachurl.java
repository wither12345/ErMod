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
    /** 当前执行动作，便于后续渲染/表现使用。 */
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
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor accessor, @NotNull DifficultyInstance instance, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        this.setItemInHand(InteractionHand.MAIN_HAND, ErModItems.RAIN_PRAYER.get().getDefaultInstance());
        return super.finalizeSpawn(accessor, instance, spawnType, groupData, tag);
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("hydroSamachurlBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        HydroSamachurlAi.updateActivity(this);
        super.customServerAiStep();
    }

    // ===== 三个技能：目前均留空，仅记录当前 Action 与看向目标 =====

    /** 近战攻击：距离过近时触发，无冷却。 */
    protected void meleeAttack(LivingEntity target) {
        this.setAction(Action.MELEE);
        this.swing(InteractionHand.MAIN_HAND);
        target.hurt(this.damageSources().mobAttack(this), (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2));
        lookAt(target);
    }

    /** 治疗技能（CallRain）：在指定位置施放，用于治疗友军或自身，带冷却。 */
    public RainCloud callRain(Vec3 pos) {
        this.setAction(Action.CONJURE_CONSTANT);
        lookAt(pos);
        RainCloud cloud = new RainCloud(this.level(), pos.add(0, 4, 0), this, 0.3f);
        this.level().addFreshEntity(cloud);
        return cloud;
    }

    /** 水流技能（Flow）：在目标处远程施放，带冷却。 */
    protected void flow(LivingEntity target) {
        this.setAction(Action.CONJURE);
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