package net.wither.er.mixins;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkPocketEntityMixin extends Projectile {
    protected FireworkPocketEntityMixin(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @ModifyArg(method = "dealExplosionDamage" , at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),index = 1)
    public float dealExplosionDamage(float damage){
        return damage * EntityHurtEvent.getLevelMultiply(EntityHurtEvent.getEntityLevel(this.getOwner()));
    }
}
