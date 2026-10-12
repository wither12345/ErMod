package net.wither.er.entity.reaction;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.wither.er.elements.Element;
import net.wither.er.entity.TracingProjectile;
import org.jetbrains.annotations.NotNull;

public class Hyperbloom extends TracingProjectile {
    public Hyperbloom(EntityType<Hyperbloom> type, Level world) {
        super(type, world);
    }

    @Override
    public void onHitEntity(@NotNull EntityHitResult result) {
        Entity entity = result.getEntity() ;
        entity.hurt(
                new DamageSource(
                        this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(Element.BLOOM) ,
                        this, this.getOwner()
                ),9 * EntityHurtEvent.getLevelMultiply(this.getOwner()));
        this.discard();
    }
}
