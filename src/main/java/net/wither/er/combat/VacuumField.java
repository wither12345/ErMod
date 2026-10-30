package net.wither.er.combat;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class VacuumField {
    /**
     * 执行一次吸引实体操作
     * <p>
     * 该方法会将所有在指定范围内的实体向中心位置移动
     * 移动距离与实体与中心位置的距离成反比
     * 会计算实体体积和击退抗性
     * 并根据这些因素计算移动距离。
     * </p>
     *
     * @param level 所在世界
     * @param center 吸引中心位置
     * @param entity 引发吸引的实体
     * @param range 吸引范围
     * @param strength 吸引强度
     */
    public static void vacuum(LevelAccessor level, Vec3 center, Entity entity, double range, double strength) {
        level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(range), e -> shouldVacuum(entity, e)).forEach(vacuumed -> {
            double kb_res = Math.min(getKbRes(vacuumed), 1);
            double size = vacuumed.getBbHeight() * vacuumed.getBbWidth() * vacuumed.getBbWidth();
            double str = (1 - kb_res) / size / entity.distanceToSqr(vacuumed) * 5;
            if(str > 1) str = 1;
            str *= strength;
            Vec3 toPush = center.subtract(vacuumed.position()).normalize().scale(str);
            vacuumed.push(toPush);
        });
    }

    private static double getKbRes(Entity entity){
        if(entity instanceof LivingEntity living){
            AttributeInstance instance = living.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
            if(instance != null) return instance.getValue();
        }
        return 0;
    }

    /**
     * 判断是否应该吸引实体
     * <p>
     * 该方法会判断实体是否应该被吸引
     * 在vacuumed是物品实体、经验球实体、或会被vacuumer伤害时，返回true
     * </p>
     * @param vacuumer 引发吸引的实体
     * @param vacuumed 被吸引的实体
     * @return 是否应该被吸引
     * @see EntityHurtEvent#shouldHurt
     */
    private static boolean shouldVacuum(Entity vacuumer, Entity vacuumed){
        return vacuumed instanceof ItemEntity || vacuumed instanceof ExperienceOrb || EntityHurtEvent.shouldHurt(vacuumer, vacuumed);
    }
}
