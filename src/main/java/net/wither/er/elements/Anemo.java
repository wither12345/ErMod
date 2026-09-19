package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.block.IBlockBehavior;

import javax.annotation.Nullable;
import java.util.Map;

public class Anemo extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("er:anemo_immune"));

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
                                            new ElementSource(category.getDefault(), ResourceLocation.parse("er:anemo.reaction"), gauge, true)
                                    ), 2.4f * EntityHurtEvent.getLevelMultiply(applier))
                    );


            BlockPos blockPos = entity.getOnPos();
            for (int i = -2; i <= 2; i++)
                for (int j = -1; j <= 1; j++)
                    for (int k = -2; k <= 2; k++) {
                        BlockPos offPos = blockPos.offset(i, j, k);
                        if (level.getBlockState(offPos).getBlock() instanceof IBlockBehavior blockBehavior) {
                            blockBehavior.er$getReactionBehavior().ifPresent(
                                    behavior -> behavior.reacted(level, offPos, self, applier, false)
                            );
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
