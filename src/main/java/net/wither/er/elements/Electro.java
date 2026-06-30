package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.init.ErModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.entity.ArcEntity;
import net.wither.er.entity.BloomEntityEntity;
import net.wither.er.entity.Hyperbloom;
import net.wither.er.init.ElementRegistry;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

import static net.minecraft.core.registries.Registries.ENTITY_TYPE;
import static net.wither.er.elements.ElementSource.ReactionKey;
import static net.wither.er.init.ElementRegistry.*;

public class Electro extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(ENTITY_TYPE, new ResourceLocation("er:electro_immune"));

    @Override
    public Category getCategory() {
        return Category.ELECTRO;
    }

    @Override
    public float reactWith(AuraContainer container, SingleElementalContainer singleElementalContainer, float strength, LevelAccessor accessor, double x, double y, double z, int level, double elemental_mastery, @Nullable EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
        float gauge_reduction = 0;
        if(singleElementalContainer.getCategory()  == Category.DENDRO && singleElementalContainer.getGauge() > 0) {
            if (singleElementalContainer.hasElement(ElementRegistry.QUICKEN.get())) {
                float multiply = 1.25f + EntityHurtEvent.getElementalMasteryMultiply(2, elemental_mastery);
                if (damageModifier != null && !damageModifier.locked) {
                    damageModifier.additional_amount += 3 * multiply * EntityHurtEvent.getLevelMultiply(level);
                }
            }
            else {
                gauge_reduction = reacting(strength , singleElementalContainer) ;
                container.addAura(new ElementSource(ElementRegistry.QUICKEN.get(), null , gauge_reduction, true) , accessor,x,y,z,level,0,null,null);
                if (damageModifier != null)
                    damageModifier.locked = true ;
            }
        }
        if (singleElementalContainer.getCategory() == Category.PYRO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer) ;
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
                                new DamageSource(accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(overload) , applier),
                                6 * EntityHurtEvent.getElementalMasteryMultiply(1, elemental_mastery) * EntityHurtEvent.getLevelMultiply(level));
                        entityiterator.setDeltaMovement(new Vec3((entityiterator.getDeltaMovement().x() * 3), (entityiterator.getDeltaMovement().y() * 2), (entityiterator.getDeltaMovement().z() * 3)));
                    }
                }
            }
        }
        if(singleElementalContainer.getCategory() == Category.PYRO && singleElementalContainer.getGauge() > 0){
            float gauge = reacting(strength,singleElementalContainer);
            container.addAura(new ElementSource(ElementRegistry.QUICKEN.get(), null , gauge, true) , accessor,x,y,z,level,0,null,null);
        }
        if (singleElementalContainer.getCategory() == Category.CRYO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(strength , singleElementalContainer) ;
            final Vec3 _center = new Vec3(x, y, z);
            List<LivingEntity> _entfound = accessor.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity_iterator : _entfound) {
                if (entity_iterator != applier) {
                    entity_iterator.hurt(
                            ElementSource.createDamageSource(
                                    accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ReactionKey) ,
                                    applier ,
                                    new ElementSource(CRYO.get(), new ResourceLocation("er.superconduct.reaction"), 0, true)
                            ),
                            5 * EntityHurtEvent.getElementalMasteryMultiply(1, elemental_mastery) * EntityHurtEvent.getLevelMultiply(level));
                    if (!entity_iterator.level().isClientSide())
                        entity_iterator.addEffect(new MobEffectInstance(ErModMobEffects.SUPERCONDUCT.get(), 100, 0, false, false));
                }
            }
        }
        return gauge_reduction ;
    }

    @Override
    public boolean shouldReact(AuraContainer container, @Nullable Entity applier) {
        if(container.getOwner() instanceof BloomEntityEntity bloom){
            if (bloom.level() instanceof ServerLevel _level) {
                Hyperbloom hyperbloom = ErModEntities.HYPERBLOOM.get().spawn(_level, bloom.getOnPos().above(2), MobSpawnType.MOB_SUMMONED);
                if(hyperbloom != null && applier != null) {
                    hyperbloom.setOwner(applier);
                    hyperbloom.push(0,0.4,0);
                }
            }
            bloom.discard();
            return false ;
        }
        return super.shouldReact(container,applier) ;
    }

    @Override
    public void tick(AuraContainer container, ElementalAura aura, LevelAccessor accessor, double x, double y, double z, int level, boolean naturalReduction) {
        super.tick(container, aura, accessor, x, y, z, level, naturalReduction);

        if(!container.getAura().get(Category.HYDRO.getId()).isEmpty()){
            float gauge = (float) Math.min(Math.min(aura.getGauge(),container.getAura().get(Category.HYDRO.getId()).getGauge()),0.4);
            if(container.getAura().get(Category.HYDRO.getId()).reduceAll(gauge))
                container.update();
            if(aura.reduce(gauge)){
                container.update();
            }
            if(container.getOwner() instanceof LivingEntity entity && entity.getPersistentData().getInt("Electro_Charged_Cd") <= 0){
                entity.getPersistentData().putInt("Electro_Charged_Cd", 20);
                LivingEntity attacker = entity.getLastAttacker();
                double elemental_mastery = 0;
                if (attacker != null) {
                    elemental_mastery = attacker.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY.get());
                }
                entity.hurt(new DamageSource(accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(electroCharged), attacker),
                                2 * EntityHurtEvent.getElementalMasteryMultiply(1, elemental_mastery) * EntityHurtEvent.getLevelMultiply(EntityHurtEvent.getEntityLevel(attacker)));
                entity.setDeltaMovement(new Vec3(0, 0, 0));
                final Vec3 _center = new Vec3(x, y, z);
                List<LivingEntity> _entfound = accessor.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (LivingEntity entityiterator : _entfound) {
                    if (EntityHurtEvent.shouldHurt(attacker, entityiterator) && entityiterator instanceof LivingEntity
                            && entityiterator.getPersistentData().getInt("Electro_Charged_Cd") <= 0) {
                        entityiterator.getPersistentData().putInt("Electro_Charged_Cd", 10);
                        if (accessor instanceof ServerLevel _level) {
                            ArcEntity entityToSpawn = ErModEntities.ARC.get().spawn(_level, BlockPos.containing(x, y + entity.getBbHeight() * 0.7, z), MobSpawnType.MOB_SUMMONED);
                            if (entityToSpawn != null) {
                                entityToSpawn.setActiveTarget(entityiterator.getId());
                                entityToSpawn.setSource(attacker);
                                entityToSpawn.setYRot(accessor.getRandom().nextFloat() * 360F);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.ELECTRO ;
    }

    public static void doElectroCharged(LivingEntity entity , LivingEntity attacker){
        if(entity instanceof AuraContainerInterface auraContainerInterface && entity.getPersistentData().getInt("Electro_Charged_Cd") <= 0){
            if (entity.isAlive()) {
                LevelAccessor world = entity.level();
                double x = entity.getX();
                double y = entity.getY();
                double z = entity.getZ();
                double elemental_mastery = 0;
                int level = 0;
                if (attacker != null) {
                    elemental_mastery = attacker.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY.get());
                    level = EntityHurtEvent.getEntityLevel(attacker);
                }
                entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(electroCharged), attacker),
                        (2 * EntityHurtEvent.getElementalMasteryMultiply(1, elemental_mastery) * EntityHurtEvent.getLevelMultiply(level)));
                entity.setDeltaMovement(new Vec3(0, 0, 0));
                {
                    final Vec3 _center = new Vec3(x, y, z);
                    List<LivingEntity> _entfound = world.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                    for (LivingEntity entityiterator : _entfound) {
                        if (EntityHurtEvent.shouldHurt(attacker, entityiterator) && entityiterator.getPersistentData().getInt("Electro_Charged_Cd") <= 0 &&
                                entityiterator instanceof AuraContainerInterface containerInterface &&
                                containerInterface.getAuraContainer().getAura().get(Category.HYDRO.getId()).getGauge() > 0
                        ) {
                            entityiterator.getPersistentData().putInt("Electro_Charged_Cd", 10);
                            if (world instanceof ServerLevel _level) {
                                ArcEntity entityToSpawn = ErModEntities.ARC.get().spawn(_level, BlockPos.containing(x, y + entity.getBbHeight() * 0.7, z), MobSpawnType.MOB_SUMMONED);
                                if (entityToSpawn != null) {
                                    entityToSpawn.setActiveTarget(entityiterator.getId());
                                entityToSpawn.setSource(attacker);
                                entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
                                }
                            }
                            if(containerInterface.getAuraContainer().getAura().get(Category.HYDRO.getId()).reduceAll(Math.min(0.4f,containerInterface.getAuraContainer().getAura().get(Category.HYDRO.getId()).getGauge())))
                                containerInterface.getAuraContainer().update();
                        }
                    }
                }

            }
        }
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
