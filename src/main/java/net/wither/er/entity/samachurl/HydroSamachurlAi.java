package net.wither.er.entity.samachurl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Optional;

/**
 * 水丘丘萨满（Hydro Samachurl）的 Brain 装配逻辑，参考 PiglinAi 的声明式写法。
 *
 * <p>它负责：
 * <ul>
 *   <li>注册自定义记忆模块（友军列表、CallRain/Flow 冷却）与友军/敌人感知器；</li>
 *   <li>把核心（CORE）活动与战斗（FIGHT）活动注册进 Brain；</li>
 *   <li>每个 tick 结束后切换当前活动（有敌人则进入 FIGHT，否则回到 CORE）。</li>
 * </ul>
 *
 * @see net.minecraft.world.entity.monster.piglin.PiglinAi
 * @see HydroSamachurlSensor
 */
public class HydroSamachurlAi {

    /** 友军实体标签：{@code #er:hilichurl}，命中该标签的生物都被视为友军。 */
    public static final TagKey<EntityType<?>> HILICHURL_TAG = TagKey.create(
        Registries.ENTITY_TYPE, new ResourceLocation("er", "hilichurl")
    );

    /** 近战触发距离（格）。 */
    public static final double MELEE_RANGE = 8;
    /** 「远离」判定距离（格），距离小于该值且大于/等于近战距离时后退。 */
    public static final double RETREAT_RANGE = 25;
    /** 血量阈值：自身或友军血量低于最大血量的该比例时施放 CallRain 治疗。 */
    public static final double LOW_HEALTH_RATIO = 0.9D;
    /** Flow 技能冷却（tick）。 */
    public static final int FLOW_COOLDOWN_TICKS = 100;
    /** 感知器扫描半径（格），用于找出附近的友军与敌人。 */
    static final double SENSOR_RANGE = 16.0D;

    /** 附近的友军（命中了 HILICHURL_TAG 的存活实体）。 */
    public static final MemoryModuleType<List<LivingEntity>> NEARBY_ALLIES = new MemoryModuleType<>(Optional.empty());
    /** CallRain 技能冷却，以过期记忆实现计时。 */
    public static final MemoryModuleType<Unit> CALL_RAIN_COOLDOWN = new MemoryModuleType<>(Optional.empty());
    /** 正在引导 CallRain 的标记：存在期间丘丘萨满将保持站立不动。 */
    public static final MemoryModuleType<Unit> CHANNELING = new MemoryModuleType<>(Optional.empty());
    /** Flow 技能冷却，以过期记忆实现计时。 */
    public static final MemoryModuleType<Unit> FLOW_COOLDOWN = new MemoryModuleType<>(Optional.empty());

    /** 自定义感知器：负责记忆友军、敌人在哪里，并在无敌人时沿袭友军的目标。 */
    public static final SensorType<HydroSamachurlSensor> HYDRO_SAMACHURL_SENSOR = new SensorType<>(HydroSamachurlSensor::new);

    /** 向 Brain 注册的记忆模块集合（含行为会用到的原版记忆模块）。 */
    public static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.ATTACK_TARGET,
        NEARBY_ALLIES, CALL_RAIN_COOLDOWN, CHANNELING, FLOW_COOLDOWN
    );
    /** 向 Brain 注册的感知器集合。 */
    public static final ImmutableList<SensorType<? extends Sensor<? super HydroSamachurl>>> SENSOR_TYPES = ImmutableList.of(
        HYDRO_SAMACHURL_SENSOR
    );

    /**
     * 生成并装配一个丘丘萨满的 Brain。
     *
     * @param mob   目标实体
     * @param brain 由 {@code brainProvider} 创建的空白 Brain
     * @return 装配完成的 Brain
     */
    protected static Brain<HydroSamachurl> makeBrain(HydroSamachurl mob, Brain<HydroSamachurl> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    /** 核心活动：待机观察、移动，以及满足血量/冷却条件时的不定时治疗行为。 */
    private static void initCoreActivity(Brain<HydroSamachurl> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.<BehaviorControl<? super HydroSamachurl>>of(
            new LookAtTargetSink(45, 90),
            new MoveToTargetSink(),
            new HydroSamachurlCallRainBehavior()
        ));
    }

    /** 闲置活动：无敌人且不做其他事时随机游荡（引导技能期间不会游荡）。 */
    private static void initIdleActivity(Brain<HydroSamachurl> brain) {
        brain.addActivity(Activity.IDLE, 10, ImmutableList.<BehaviorControl<? super HydroSamachurl>>of(
            new HydroSamachurlWander()
        ));
    }

    /** 战斗活动：远程 Flow（高优先级）+ 近战 / 远离（低优先级，Flow 期间自动让位）。 */
    private static void initFightActivity(Brain<HydroSamachurl> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
            Activity.FIGHT,
            15,
            ImmutableList.<BehaviorControl<? super HydroSamachurl>>of(new HydroSamachurlFlowBehavior()),
            MemoryModuleType.ATTACK_TARGET
        );
        brain.addActivityAndRemoveMemoryWhenStopped(
            Activity.FIGHT,
            10,
            ImmutableList.<BehaviorControl<? super HydroSamachurl>>of(new HydroSamachurlFightBehavior()),
            MemoryModuleType.ATTACK_TARGET
        );
    }

    /**
     * 每 tick 刷新当前活动：有敌人时切换到 FIGHT，否则回落到 IDLE 随机游荡。
     *
     * @param mob 目标实体
     */
    protected static void updateActivity(HydroSamachurl mob) {
        mob.getBrain().setActiveActivityIfPossible(Activity.FIGHT);
    }

    /** 是否正处于技能引导中（引导期间应保持站立不动）。 */
    public static boolean isChanneling(HydroSamachurl mob) {
        return mob.getBrain().hasMemoryValue(CHANNELING);
    }
}