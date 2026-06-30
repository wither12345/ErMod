package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.wither.er.entity.BloomEntityEntity;
import net.wither.er.init.ElementRegistry;

import javax.annotation.Nullable;

import static net.minecraft.core.registries.Registries.ENTITY_TYPE;

public class Hydro extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(ENTITY_TYPE, new ResourceLocation("er:hydro_immune"));

    @Override
    public Category getCategory() {
        return Category.HYDRO;
    }

    @Override
    public float reactWith(AuraContainer container, SingleElementalContainer singleElementalContainer, float strength, LevelAccessor accessor, double x, double y, double z, int level, double elemental_mastery, @Nullable EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
        float multiply = 1;
        float gauge_reduction = 0 ;
        if (singleElementalContainer.getCategory() == Category.PYRO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer , 2) ;
            if (damageModifier != null && !damageModifier.locked) {
                multiply = 2 + EntityHurtEvent.getElementalMasteryMultiply(0, elemental_mastery);
                damageModifier.locked = true;
            }
        }
        if (singleElementalContainer.getCategory() == Category.CRYO && singleElementalContainer.getGaugeExcept(ElementRegistry.FROZEN.get()) > 0){
            gauge_reduction = reactingExcept(strength, singleElementalContainer, ElementRegistry.FROZEN.get()) ;
            container.addAura(new ElementSource(ElementRegistry.FROZEN.get(), null , 2 * gauge_reduction, true) , accessor,x,y,z,level,0,null,applier);
        }
        if (singleElementalContainer.getCategory() == Category.DENDRO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer , 0.5f) ;
            if (accessor instanceof ServerLevel _level) {
                BloomEntityEntity entityToSpawn = ErModEntities.BLOOM_ENTITY.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                if(entityToSpawn != null) {
                    entityToSpawn.setOwner(applier);
                    entityToSpawn.moveTo(x, y, z, accessor.getRandom().nextFloat() * 360F, 0);
                    entityToSpawn.setYRot(accessor.getRandom().nextFloat() * 360F);
                }
                _level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 5, 1, 1, 1, 0);
            }
        }
        if (damageModifier != null) {
            damageModifier.reaction_multiply *= multiply ;
        }
        return gauge_reduction ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.HYDRO ;
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
