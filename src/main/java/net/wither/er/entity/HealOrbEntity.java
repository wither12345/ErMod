package net.wither.er.entity;

import net.mcreator.er.ErMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class HealOrbEntity extends BuffOrbEntity{
    public HealOrbEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        ErMod.LOGGER.info(this.getUUID());
    }

    @Override
    protected void onTouch(Entity entity) {
        if(entity instanceof LivingEntity living)
            living.heal(living.getMaxHealth() * 0.1f);
    }
}
