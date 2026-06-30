package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.entity.BloomEntityEntity;
import net.wither.er.init.ElementRegistry;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

import static net.minecraft.core.registries.Registries.ENTITY_TYPE;

public class Pyro extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(ENTITY_TYPE, new ResourceLocation("er:pyro_immune"));

    @Override
    public Category getCategory() {
        return Category.PYRO ;
    }

    @Override
    public float reactWith(AuraContainer container, SingleElementalContainer singleElementalContainer, float strength, LevelAccessor accessor, double x, double y, double z, int level, double elemental_mastery, @Nullable EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
        float multiply = 1;
        float gauge_reduction = 0 ;
        if (singleElementalContainer.getCategory() == Category.HYDRO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer , 0.5f) ;
            if (damageModifier != null && !damageModifier.locked) {
                multiply = 1.5f + EntityHurtEvent.getElementalMasteryMultiply(0, elemental_mastery);
                damageModifier.locked = true;
            }
        }
        if (singleElementalContainer.getCategory() == Category.CRYO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer , 2) ;
            if (damageModifier != null && !damageModifier.locked) {
                multiply = 2 + EntityHurtEvent.getElementalMasteryMultiply(0, elemental_mastery);
                damageModifier.locked = true;
            }
        }
        if(singleElementalContainer.getCategory() == Category.DENDRO && singleElementalContainer.getGauge() > 0){
            container.addAura(new ElementSource(ElementRegistry.BURNING.get(), null , 1.6f, true) , accessor,x,y,z,level,0,null,applier);
        }
        if (singleElementalContainer.getCategory() == Category.ELECTRO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer) ;
            if (damageModifier != null && !damageModifier.locked) {
                if (accessor instanceof ServerLevel _level)
                    _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, 0, 0, 0, 0);
                if (accessor instanceof Level _level) {
                    if (!_level.isClientSide()) {
                        _level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.generic.explode")), SoundSource.NEUTRAL, 1, 1);
                    } else {
                        _level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.generic.explode")), SoundSource.NEUTRAL, 1, 1, false);
                    }
                }
                {
                    final Vec3 _center = new Vec3(x, y, z);
                    List<LivingEntity> _entfound = accessor.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                    for (LivingEntity entityiterator : _entfound) {
                        if (EntityHurtEvent.shouldHurt(applier, entityiterator)) {
                            entityiterator.hurt(
                                    new DamageSource(
                                            accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(overload),
                                            applier),
                                    6 * EntityHurtEvent.getElementalMasteryMultiply(1, elemental_mastery) * EntityHurtEvent.getLevelMultiply(level));
                            entityiterator.setDeltaMovement(new Vec3((entityiterator.getDeltaMovement().x() * 3), (entityiterator.getDeltaMovement().y() * 2), (entityiterator.getDeltaMovement().z() * 3)));
                        }
                    }
                }
                damageModifier.locked = true;
            }
        }
        if (damageModifier != null) {
            damageModifier.reaction_multiply *= multiply ;
        }
        return gauge_reduction ;
    }

    @Override
    public boolean shouldReact(AuraContainer container, @Nullable Entity applier) {
        if(container.getOwner() instanceof BloomEntityEntity bloom){
            bloom.explode(9f, applier);
            return false ;
        }
        return super.shouldReact(container,applier) ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.PYRO ;
    }

    @Override
    public boolean overrideReduceRate() {
        return true;
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
