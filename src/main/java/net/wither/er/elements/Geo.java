package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.entity.LunarCrystallize;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class Geo extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("er:geo_immune"));

    public Geo() {
        super(Map.of());
    }


    @Override
    public Category getCategory() {
        return Category.GEO;
    }

    public static float pyro(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        spawnCrystallize(ErModEntities.PYRO_CRYSTALLIZE.get(), auraContainer, applier);
        return reacting(source , boundAura , 0.5f) ;
    }

    public static float hydro(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        if(isLunar(applier) && auraContainer.getOwner() instanceof Entity entity && entity.level() instanceof ServerLevel level) {
            final Vec3 _center = entity.position();
            List<LunarCrystallize> crystallizes = level.getEntitiesOfClass(LunarCrystallize.class, new AABB(_center, _center).inflate(20), e -> true);
            if (crystallizes.isEmpty()) {
                LunarCrystallize crystallize = ErModEntities.LUNAR_CRYSTALLIZE.get().spawn(level, entity.getOnPos(), MobSpawnType.MOB_SUMMONED);
                if(crystallize != null) {
                    crystallize.modifyPos();
                    crystallize.setOwner(applier);
                }
            } else {
                crystallizes.forEach(crystallize -> crystallize.add(applier));
            }
        }
        else
            spawnCrystallize(ErModEntities.HYDRO_CRYSTALLIZE.get(), auraContainer, applier);
        return reacting(source , boundAura , 0.5f) ;
    }

    public static float cryo(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        spawnCrystallize(ErModEntities.CRYO_CRYSTALLIZE.get(), auraContainer, applier);
        return reacting(source , boundAura , 0.5f) ;
    }

    public static float electro(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        spawnCrystallize(ErModEntities.ELECTRO_CRYSTALLIZE.get(), auraContainer, applier);
        return reacting(source , boundAura , 0.5f) ;
    }

    public static void spawnCrystallize(EntityType<?> type, AuraContainer auraContainer, @Nullable Entity applier){
        if (auraContainer.getOwner() instanceof Entity entity && entity.level() instanceof ServerLevel _level) {
            Entity entityToSpawn = type.spawn(_level, entity.getOnPos().above(), MobSpawnType.MOB_SUMMONED);
            if (entityToSpawn != null) {
                if (applier != null) {
                    entityToSpawn.getPersistentData().putString("UUID", applier.getStringUUID());
                    entityToSpawn.getPersistentData().putFloat("health", 5 * EntityHurtEvent.getEntityLevel(applier) * EntityHurtEvent.getElementalMasteryMultiply(1,EntityHurtEvent.getElementalMastery(applier)));
                }
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
