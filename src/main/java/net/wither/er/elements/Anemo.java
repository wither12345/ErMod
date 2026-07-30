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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Map;

public class Anemo extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("er:anemo_immune"));

    public Anemo(){
        super(Map.of());
    }

    @Override
    public Category getCategory() {
        return Category.ANEMO;
    }

    protected static float swirl(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier)
    {
        Category category = self.getCategory();
        float gauge = reacting(source, boundAura , 0.5f) ;
        if(auraContainer.getOwner() instanceof Entity entity) {
            Vec3 pos = entity.position();
            Level level = entity.level();
            if (level instanceof ServerLevel _level)
                _level.sendParticles(getParticle(category), (pos.x - 1.5), pos.y + 1, (pos.z - 1.5), 8, 1.5, 0, 1.5, 0);

            if (modifier != null) {
                if (modifier.locked) return 0;
                modifier.locked = true;
            }

            entity.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos, pos).inflate(3)).stream()
                    .filter(e -> EntityHurtEvent.shouldHurt(applier, e))
                    .forEach(
                            e -> e.hurt(
                                    ElementSource.createDamageSource(
                                            entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SWIRL),
                                            applier,
                                            new ElementSource(category.getDefault(), new ResourceLocation("er:anemo.reaction"), gauge, true)
                                    ), 2.4f * EntityHurtEvent.getLevelMultiply(applier))
                    );

            if (category == Category.PYRO) {
                BlockPos blockPos = entity.getOnPos();
                for (int i = -2; i <= 2; i++)
                    for (int j = -2; j <= 2; j++)
                        for (int k = -1; k <= 1; k++) {
                            BlockPos offPos = blockPos.offset(i, j, k);
                            if ((level.getBlockState(offPos).getBlock() == Blocks.GRASS_BLOCK)) {
                                level.setBlock(offPos, ErModBlocks.BURNING_DIRT.get().defaultBlockState(), 3);
                                if (!level.isClientSide()) {
                                    BlockEntity _blockEntity = level.getBlockEntity(offPos);
                                    BlockState _bs = level.getBlockState(offPos);
                                    if (_blockEntity != null)
                                        if (applier != null) {
                                            _blockEntity.getPersistentData().putString("UUID", applier.getStringUUID());
                                        }
                                    level.sendBlockUpdated(offPos, _bs, _bs, 3);
                                }
                            }
                        }
            }
        }
        return gauge;
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
