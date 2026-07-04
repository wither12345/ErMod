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
import java.util.Map;

public class Geo extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("er:geo_immune"));

    public Geo() {
        super(Map.of(
                Category.PYRO, Geo::pyro,
                Category.HYDRO, Geo::hydro,
                Category.CRYO, Geo::cryo,
                Category.ELECTRO, Geo::electro
        ));
    }


    @Override
    public Category getCategory() {
        return Category.GEO;
    }

    public static float pyro(AuraContainer container , SingleElementalContainer singleElementalContainer , float gauge, LevelAccessor accessor , double x , double y , double z, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier){
        spawnCrystallize(ErModEntities.PYRO_CRYSTALLIZE.get(), accessor, x, y, z, applier);
        return reacting(gauge , singleElementalContainer , 0.5f) ;
    }

    public static float hydro(AuraContainer container , SingleElementalContainer singleElementalContainer , float gauge, LevelAccessor accessor , double x , double y , double z, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier){
        spawnCrystallize(ErModEntities.HYDRO_CRYSTALLIZE.get(), accessor, x, y, z, applier);
        return reacting(gauge , singleElementalContainer , 0.5f) ;
    }

    public static float cryo(AuraContainer container , SingleElementalContainer singleElementalContainer , float gauge, LevelAccessor accessor , double x , double y , double z, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier){
        spawnCrystallize(ErModEntities.CRYO_CRYSTALLIZE.get(), accessor, x, y, z, applier);
        return reacting(gauge , singleElementalContainer , 0.5f) ;
    }

    public static float electro(AuraContainer container , SingleElementalContainer singleElementalContainer , float gauge, LevelAccessor accessor , double x , double y , double z, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier){
        spawnCrystallize(ErModEntities.ELECTRO_CRYSTALLIZE.get(), accessor, x, y, z, applier);
        return reacting(gauge , singleElementalContainer , 0.5f) ;
    }

    public static void spawnCrystallize(EntityType<?> type, LevelAccessor accessor, double x, double y, double z, @Nullable Entity applier){
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
