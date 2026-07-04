package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
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
import java.util.Map;

public class Electro extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("er:electro_immune"));

    public Electro() {
        super(Map.of(
                Category.DENDRO, Electro::dendro,
                Category.PYRO, Element::overLoad,
                Category.CRYO, Element::superconduct
        ));
    }

    @Override
    public Category getCategory() {
        return Category.ELECTRO;
    }

    private static float dendro(AuraContainer container ,
                                SingleElementalContainer singleElementalContainer ,
                                float gauge,
                                LevelAccessor accessor ,
                                double x ,
                                double y ,
                                double z,
                                EntityHurtEvent.DamageModifier damageModifier,
                                @Nullable Entity applier
    ){
        if (singleElementalContainer.hasElement(ElementRegistry.QUICKEN.get())) {
            float multiply = 1.25f * EntityHurtEvent.ReactionMultiply.CATALYZE.getMulti(applier);
            if (damageModifier != null && !damageModifier.locked) {
                damageModifier.additional_amount += 3 * multiply * EntityHurtEvent.getLevelMultiply(applier);
            }
        }
        else {
            float gauge_reduction = reacting(gauge , singleElementalContainer) ;
            container.addAura(new ElementSource(ElementRegistry.QUICKEN.get(), null , gauge_reduction, true) , accessor,x,y,z,null,null);
            if (damageModifier != null)
                damageModifier.locked = true ;
            return gauge_reduction ;
        }
        return 0;
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
    public void tick(AuraContainer container, ElementalAura aura, LevelAccessor accessor, double x, double y, double z, boolean naturalReduction) {
        super.tick(container, aura, accessor, x, y, z, naturalReduction);

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
                entity.hurt
                        (ElementSource.createDamageSource(accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ELECTRO_CHARGED) , attacker ,
                                new ElementSource(ElementRegistry.ELECTRO.get() , new ResourceLocation("er.electro_charged.reaction") , 0, true)
                        ),(8 * EntityHurtEvent.getLevelMultiply(EntityHurtEvent.getEntityLevel(attacker)))
                );
                entity.setDeltaMovement(new Vec3(0, 0, 0));
                {
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
                entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ELECTRO_CHARGED), attacker),
                        (8 * EntityHurtEvent.getLevelMultiply(attacker)));
                entity.setDeltaMovement(new Vec3(0, 0, 0));
                final Vec3 _center = new Vec3(x, y, z);
                List<LivingEntity> newfound = world.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().toList();
                for (LivingEntity living : newfound) {
                    if (EntityHurtEvent.shouldHurt(attacker, living) && living.getPersistentData().getInt("Electro_Charged_Cd") <= 0 &&
                            living instanceof AuraContainerInterface containerInterface &&
                            containerInterface.er$getAuraContainer().getAura().get(Category.HYDRO.getId()).getGauge() > 0
                    ) {
                        living.getPersistentData().putInt("Electro_Charged_Cd", 10);
                        if (world instanceof ServerLevel _level) {
                            ArcEntity entityToSpawn = ErModEntities.ARC.get().spawn(_level, BlockPos.containing(x, y + entity.getBbHeight() * 0.7, z), MobSpawnType.MOB_SUMMONED);
                            if (entityToSpawn != null) {
                                entityToSpawn.setActiveTarget(living.getId());
                            entityToSpawn.setSource(attacker);
                            entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
                            }
                        }
                        if(containerInterface.er$getAuraContainer().getAura().get(Category.HYDRO.getId()).reduceAll(Math.min(0.4f,containerInterface.er$getAuraContainer().getAura().get(Category.HYDRO.getId()).getGauge())))
                            containerInterface.er$getAuraContainer().update();
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
