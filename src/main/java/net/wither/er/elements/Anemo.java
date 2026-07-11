package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModBlocks;
import net.minecraft.core.BlockPos;
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

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Anemo extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("er:anemo_immune"));

    public Anemo(){
        super(Map.of(
                Category.PYRO, Anemo::swirl,
                Category.CRYO, Anemo::swirl,
                Category.ELECTRO, Anemo::swirl,
                Category.HYDRO, Anemo::swirl
        ));
    }

    @Override
    public Category getCategory() {
        return Category.ANEMO;
    }

    private static float swirl(AuraContainer container ,
                               SingleElementalContainer singleElementalContainer ,
                               float gauge,
                               LevelAccessor accessor ,
                               double x ,
                               double y ,
                               double z,
                               EntityHurtEvent.DamageModifier damageModifier,
                               @Nullable Entity applier){
        Category category = singleElementalContainer.getCategory();
        if (accessor instanceof ServerLevel _level)
            _level.sendParticles(getParticle(category), (x - 1.5), y + 1, (z - 1.5), 8, 1.5, 0, 1.5, 0);

        if (damageModifier != null) {
            if(damageModifier.locked) return 0;
            damageModifier.locked = true ;
        }
        final Vec3 _center = new Vec3(x, y, z);
        List<LivingEntity> _entfound = accessor.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (LivingEntity entityiterator : _entfound) {
            if (EntityHurtEvent.shouldHurt(applier, entityiterator))
                entityiterator.hurt(
                        ElementSource.createDamageSource(
                                accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SWIRL) ,
                                applier ,
                                new ElementSource(category.getDefault(), new ResourceLocation("er:anemo.reaction") , gauge, true)
                        ),2.4f * EntityHurtEvent.getLevelMultiply(applier));
        }

        if(category == Category.PYRO){
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
        return reacting(gauge , singleElementalContainer , 0.5f) ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.ANEMO ;
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
