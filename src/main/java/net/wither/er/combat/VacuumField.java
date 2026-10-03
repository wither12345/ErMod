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
    public static void vacuum(LevelAccessor world, Vec3 center, Entity entity, double range, double strength) {
        world.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(range), e -> shouldVacuum(entity, e)).forEach(vacuumed -> {
            double kb_res = Math.min(getKbRes(vacuumed), 1);
            double size = vacuumed.getBbHeight() * vacuumed.getBbWidth() * vacuumed.getBbWidth();
            double str = strength * (1 - kb_res) / size / entity.distanceToSqr(vacuumed) * 5;
            if(str > 1) str = 1;
            Vec3 toPush = center.subtract(vacuumed.position()).normalize().scale(str);
            vacuumed.push(toPush);
        });
    }

    public static double getKbRes(Entity entity){
        if(entity instanceof LivingEntity living){
            AttributeInstance instance = living.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
            if(instance != null) return instance.getValue();
        }
        return 0;
    }

    public static boolean shouldVacuum(Entity vacuumer, Entity vacuumed){
        return vacuumed instanceof ItemEntity || vacuumed instanceof ExperienceOrb || EntityHurtEvent.shouldHurt(vacuumer, vacuumed);
    }
}
