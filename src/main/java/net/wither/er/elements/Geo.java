package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

public class Geo extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("er:geo_immune"));


    @Override
    public Category getCategory() {
        return Category.GEO;
    }

    @Override
    public float reactWith(AuraContainer container, SingleElementalContainer singleElementalContainer, float strength, LevelAccessor accessor, double x, double y, double z, int level, double elemental_mastery, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
        float gauge_reduction = 0 ;
        if (singleElementalContainer.getCategory() == Category.PYRO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer , 0.5f) ;
            if (accessor instanceof ServerLevel _level) {
                Entity entityToSpawn = ErModEntities.PYRO_CRYSTALLIZE.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                if (entityToSpawn != null) {
                    if (applier != null) {
                        entityToSpawn.getPersistentData().putString("UUID", applier.getStringUUID());
                        entityToSpawn.getPersistentData().putFloat("health", 5 * EntityHurtEvent.getEntityLevel(applier) * EntityHurtEvent.getElementalMasteryMultiply(1,EntityHurtEvent.getElementalMastery(applier)));
                    }
                    entityToSpawn.setYRot(accessor.getRandom().nextFloat() * 360F);
                }
            }
        }
        if (singleElementalContainer.getCategory() == Category.HYDRO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer , 0.5f) ;
            if (accessor instanceof ServerLevel _level) {
                Entity entityToSpawn = ErModEntities.HYDRO_CRYSTALLIZE.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                if(entityToSpawn != null) {
                    if (applier != null) {
                        entityToSpawn.getPersistentData().putString("UUID", applier.getStringUUID());
                        entityToSpawn.getPersistentData().putFloat("health", 5 * EntityHurtEvent.getEntityLevel(applier) * EntityHurtEvent.getElementalMasteryMultiply(1,EntityHurtEvent.getElementalMastery(applier)));
                    }
                    entityToSpawn.setYRot(accessor.getRandom().nextFloat() * 360F);
                }
            }
        }
        if (singleElementalContainer.getCategory() == Category.ELECTRO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer , 0.5f) ;
            if (accessor instanceof ServerLevel _level) {
                Entity entityToSpawn = ErModEntities.ELECTRO_CRYSTALLIZE.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                if (entityToSpawn != null) {
                    if (applier != null) {
                        entityToSpawn.getPersistentData().putString("UUID", applier.getStringUUID());
                        entityToSpawn.getPersistentData().putFloat("health", 5 * EntityHurtEvent.getEntityLevel(applier) * EntityHurtEvent.getElementalMasteryMultiply(1,EntityHurtEvent.getElementalMastery(applier)));
                    }
                    entityToSpawn.setYRot(accessor.getRandom().nextFloat() * 360F);
                }
            }
        }
        if (singleElementalContainer.getCategory() == Category.CRYO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer , 0.5f) ;
            if (accessor instanceof ServerLevel _level) {
                Entity entityToSpawn = ErModEntities.CRYO_CRYSTALLIZE.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                if (entityToSpawn != null) {
                    if (applier != null) {
                        entityToSpawn.getPersistentData().putString("UUID", applier.getStringUUID());
                        entityToSpawn.getPersistentData().putFloat("health", 5 * EntityHurtEvent.getEntityLevel(applier) * EntityHurtEvent.getElementalMasteryMultiply(1,EntityHurtEvent.getElementalMastery(applier)));
                    }
                    entityToSpawn.setYRot(accessor.getRandom().nextFloat() * 360F);
                }
            }
        }
        return gauge_reduction ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.GEO ;
    }

    @Override
    public boolean isApplicable() {
        return false ;
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
