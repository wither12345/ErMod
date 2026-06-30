package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModBlocks;
import net.mcreator.er.init.ErModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.init.ElementRegistry;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

import static net.wither.er.elements.ElementSource.ReactionKey;

public class Anemo extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("er:anemo_immune"));

    @Override
    public Category getCategory() {
        return Category.ANEMO;
    }

    @Override
    public float reactWith(AuraContainer container, SingleElementalContainer singleElementalContainer, float strength, LevelAccessor accessor, double x, double y, double z, int level, double elemental_mastery, @Nullable EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
        float strength_reduction = 0 ;
        if (singleElementalContainer.getCategory() == Category.CRYO && singleElementalContainer.getGauge() > 0) {
            strength_reduction = reacting(strength , singleElementalContainer , 0.5f) ;
            spread(ElementRegistry.CRYO.get(), strength_reduction * 2 , accessor,x,y,z,level,elemental_mastery,damageModifier,applier);
        }
        if (singleElementalContainer.getCategory() == Category.ELECTRO && singleElementalContainer.getGauge() > 0) {
            strength_reduction = reacting(strength , singleElementalContainer , 0.5f) ;
            spread(ElementRegistry.ELECTRO.get(), strength_reduction * 2 , accessor,x,y,z,level,elemental_mastery,damageModifier,applier);
        }
        if (singleElementalContainer.getCategory() == Category.HYDRO && singleElementalContainer.getGauge() > 0) {
            strength_reduction = reacting(strength , singleElementalContainer , 0.5f) ;
            spread(ElementRegistry.HYDRO.get(), strength_reduction * 2 , accessor,x,y,z,level,elemental_mastery,damageModifier,applier);
        }
        if (singleElementalContainer.getCategory() == Category.PYRO && singleElementalContainer.getGauge() > 0) {
            strength_reduction = reacting(strength , singleElementalContainer , 0.5f) ;
            spread(ElementRegistry.PYRO.get(), strength_reduction * 2 , accessor,x,y,z,level,elemental_mastery,damageModifier,applier);
        }
        return strength_reduction ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.ANEMO ;
    }

    @Override
    public boolean isApplicable() {
        return false ;
    }

    public static void spread(Element element , float gauge, LevelAccessor accessor, double x, double y, double z, int level, double elemental_mastery, @Nullable EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier){
        if (accessor instanceof ServerLevel _level)
            _level.sendParticles(getParticle(element.getCategory()), (x - 1.5), y + 1, (z - 1.5), 8, 1.5, 0, 1.5, 0);

        if (damageModifier != null) {
            if(damageModifier.locked) return;
            damageModifier.locked = true ;
        }
        final Vec3 _center = new Vec3(x, y, z);
        List<LivingEntity> _entfound = accessor.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (LivingEntity entityiterator : _entfound) {
            if (EntityHurtEvent.shouldHurt(applier, entityiterator))
                entityiterator.hurt(
                        ElementSource.createDamageSource(
                                accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ReactionKey) ,
                                applier ,
                                new ElementSource(element , ResourceLocation.parse("er:anemo.reaction") , gauge, true)
                        ),3 * EntityHurtEvent.getElementalMasteryMultiply(1, elemental_mastery) * EntityHurtEvent.getLevelMultiply(level));
        }

        if(element.getCategory() == Category.PYRO){
            for (int i = -2; i <= 2; i++)
                for (int j = -2; j <= 2; j++)
                    for (int k = -1; k <= 1; k++)
                        if ((accessor.getBlockState(BlockPos.containing(x + i, y + k, z + j))).getBlock() == Blocks.GRASS_BLOCK) {
                            accessor.setBlock(BlockPos.containing(x + i, y + k, z + j), ErModBlocks.BURNING_DIRT.get().defaultBlockState(), 3);
                            if (!accessor.isClientSide()) {
                                BlockPos _bp = BlockPos.containing(x + i, y + k, z + j);
                                BlockEntity _blockEntity = accessor.getBlockEntity(_bp);
                                BlockState _bs = accessor.getBlockState(_bp);
                                if (_blockEntity != null)
                                    if (applier != null) {
                                        _blockEntity.getPersistentData().putString("UUID", applier.getStringUUID());
                                    }
                                if (accessor instanceof Level _level)
                                    _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                            }
                        }
        }
    }

    private static SimpleParticleType getParticle(Element.Category category){
        if(category == Category.HYDRO)
            return ErModParticleTypes.SMALL_HYDRO_PARTICLE.get();
        if(category == Category.ELECTRO)
            return ErModParticleTypes.SMALL_ELECTRO_PARTICLE.get();
        if(category == Category.PYRO)
            return ErModParticleTypes.SMALL_PYRO_PARTICLE.get();
        if(category == Category.CRYO)
            return ErModParticleTypes.SMALL_CRYO_PARTICLE.get();
        return ErModParticleTypes.SMALL_ANEMO_PARTICLE.get();
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
