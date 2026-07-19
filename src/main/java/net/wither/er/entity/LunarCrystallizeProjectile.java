package net.wither.er.entity;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

public class LunarCrystallizeProjectile extends TracingProjectile{
    public LunarCrystallizeProjectile(EntityType<LunarCrystallizeProjectile> type, Level world) {
        super(type, world);
    }

    @Override
    public void onHitEntity(@NotNull EntityHitResult result) {
        Entity entity = result.getEntity() ;
        entity.hurt(new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(Element.LUNAR_CRYSTALLIZE) , this, this.getOwner()),12 * EntityHurtEvent.getLevelMultiply(this.getOwner()));
        this.discard();
    }
}
