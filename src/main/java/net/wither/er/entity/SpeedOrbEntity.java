package net.wither.er.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SpeedOrbEntity extends BuffOrbEntity{
    public SpeedOrbEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onTouch(Entity entity) {
        if(entity instanceof LivingEntity living)
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400));
    }
}
