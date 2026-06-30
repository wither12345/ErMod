package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.init.ErModMobEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.init.ElementRegistry;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static net.minecraft.core.registries.Registries.ENTITY_TYPE;
import static net.wither.er.init.ElementRegistry.CRYO;
import static net.wither.er.elements.ElementSource.ReactionKey;

public class Cryo extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(ENTITY_TYPE, new ResourceLocation("er:cryo_immune"));

    @Override
    public Category getCategory() {
        return Category.CRYO;
    }

    @Override
    public float reactWith(AuraContainer container, SingleElementalContainer singleElementalContainer, float gauge, LevelAccessor accessor, double x, double y, double z, int level, double elemental_mastery, @Nullable EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
        float multiply = 1;
        float gauge_reduction = 0 ;
        if (applier instanceof LivingEntity && ((LivingEntity) applier).getAttribute(ErModAttributes.ELEMENTAL_MASTERY.get()) != null)
            elemental_mastery = Objects.requireNonNull(((LivingEntity) applier).getAttribute(ErModAttributes.ELEMENTAL_MASTERY.get())).getValue();
        if (singleElementalContainer.getCategory() == Category.PYRO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(gauge , singleElementalContainer , 0.5f) ;
            if (damageModifier != null && !damageModifier.locked) {
                multiply = 1.5f + EntityHurtEvent.getElementalMasteryMultiply(0, elemental_mastery);
                damageModifier.locked = true;
            }
        }
        if (singleElementalContainer.getCategory() == Category.HYDRO && singleElementalContainer.getGauge() > 0){
            gauge_reduction = reacting(gauge , singleElementalContainer) ;
            container.addAura(new ElementSource(ElementRegistry.FROZEN.get(), null , gauge_reduction * 2, true) , accessor,x,y,z,level,0,null,applier);
        }
        if (singleElementalContainer.getCategory() == Category.ELECTRO && singleElementalContainer.getGauge() > 0) {
            gauge_reduction = reacting(gauge , singleElementalContainer) ;
            if (damageModifier != null && !damageModifier.locked) {
                final Vec3 _center = new Vec3(x, y, z);
                List<LivingEntity> _entfound = accessor.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (LivingEntity entity_iterator : _entfound) {
                    if (entity_iterator != applier) {
                        entity_iterator.hurt(
                                ElementSource.createDamageSource(
                                        accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ReactionKey),
                                        applier,
                                        new ElementSource(CRYO.get(), new ResourceLocation("er.superconduct.reaction"), 0, true)
                                ),
                                5 * EntityHurtEvent.getElementalMasteryMultiply(1, elemental_mastery) * EntityHurtEvent.getLevelMultiply(level));
                        if (!entity_iterator.level().isClientSide())
                            entity_iterator.addEffect(new MobEffectInstance(ErModMobEffects.SUPERCONDUCT.get(), 100, 0, false, false));
                    }
                }
                damageModifier.locked = true ;
            }
        }
        if(damageModifier != null)
            damageModifier.multiply *= multiply;
        return gauge_reduction ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.CRYO ;
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
