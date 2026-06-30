package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
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

public class Dendro extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("er:dendro_immune"));

    @Override
    public Category getCategory() {
        return Category.DENDRO;
    }

    @Override
    public float reactWith(AuraContainer container, SingleElementalContainer singleContainer, float strength, LevelAccessor accessor, double x, double y, double z, int level, double elemental_mastery, @Nullable EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
        float gauge_reduction = 0 ;
        if(singleContainer.hasElement(ElementRegistry.QUICKEN.get()) && singleContainer.getGauge() > 0) {
                float multiply = 1.25f + EntityHurtEvent.getElementalMasteryMultiply(2, elemental_mastery);
                if (damageModifier != null && !damageModifier.locked) {
                    damageModifier.additional_amount += 3 * multiply * EntityHurtEvent.getLevelMultiply(level);
                }
            }
        if (singleContainer.getCategory() == Category.HYDRO && singleContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleContainer , 2) ;
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
        if(singleContainer.getCategory() == Category.ELECTRO && singleContainer.getGauge() > 0){
            gauge_reduction = reacting(strength , singleContainer) ;
            container.addAura(new ElementSource(ElementRegistry.QUICKEN.get(), null , gauge_reduction, true) , accessor,x,y,z,level,0,null,applier);
        }
        if(singleContainer.getCategory() == Category.PYRO && singleContainer.getGauge() > 0){
            container.addAura(new ElementSource(ElementRegistry.BURNING.get(), null , 1.6f, true) , accessor,x,y,z,level,0,null,applier);
        }
        return gauge_reduction ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.DENDRO ;
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
