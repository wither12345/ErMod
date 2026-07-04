package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.init.ElementRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class Burning extends Element{
    public Burning() {
        super(Map.of());
    }

    @Override
    public Category getCategory() {
        return Category.PYRO;
    }

    @Override
    public float getReduceRate(float gauge) {
        return 0;
    }

    @Override
    public void start(AuraContainer container){
        super.start(container);
        SingleElementalContainer dendroContainer = container.getAura().get(Category.DENDRO.getId());
        dendroContainer.disableNaturalReduction();
    }

    @Override
    public void end(AuraContainer container) {
        super.end(container);
        SingleElementalContainer dendroContainer = container.getAura().get(Category.DENDRO.getId());
        dendroContainer.enableNaturalReduction();
    }

    @Override
    public void tick(AuraContainer container, ElementalAura aura, LevelAccessor accessor, double x, double y, double z, boolean naturalReduction) {
        super.tick(container, aura, accessor, x, y, z, naturalReduction);
        SingleElementalContainer dendroContainer = container.getAura().get(Category.DENDRO.getId());
        if(dendroContainer.isEmpty()){
            aura.setGauge(-1);
        }
        else {
            if(dendroContainer.reduceAll(0.02f))
                container.update();
            if(aura.tick % 5 == 0){
                Entity applier = aura.getApplier() ;
                burn(accessor, x, y, z, applier);
            }
        }
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.BURNING ;
    }

    public static void burn(LevelAccessor accessor, double x, double y, double z, @Nullable Entity applier){
        final Vec3 _center = new Vec3(x, y, z);
        List<LivingEntity> _entfound = accessor.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(1), LivingEntity::isAlive).stream().toList();
        double elemental_mastery = 0f ;
        int level = EntityHurtEvent.getEntityLevel(applier) ;
        if(applier instanceof LivingEntity living && living.getAttribute(ErModAttributes.ELEMENTAL_MASTERY) != null){
            elemental_mastery = living.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY);
        }
        if (accessor instanceof ServerLevel _level)
            _level.sendParticles(ParticleTypes.FLAME, x - 0.5, y, z - 0.5, 10, 1, 1, 1, 0);

        for (LivingEntity entityiterator : _entfound) {
            if(applier == null) {
                if(entityiterator.getAttribute(ErModAttributes.ELEMENTAL_MASTERY) != null)
                        elemental_mastery = entityiterator.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY);
                level = EntityHurtEvent.getEntityLevel(entityiterator) ;
                }
            entityiterator.hurt(
                    ElementSource.createDamageSource(
                            accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(BURNING),
                            applier,
                            new ElementSource(ElementRegistry.PYRO.get(), ResourceLocation.parse("er.burning.reaction"), 0.8f, true)
                    ), 1 * EntityHurtEvent.getLevelMultiply(level));
        }
    }
}
