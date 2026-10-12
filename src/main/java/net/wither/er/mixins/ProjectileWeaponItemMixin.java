package net.wither.er.mixins;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.wither.er.item.IProjectileWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin implements IItemExtension , IProjectileWeapon {
    @Unique double er$damage = 4;
    @Unique double er$damageScale = 0.33 ;

    @Override
    public void er$setDamage(double damage){
        this.er$damage = damage ;
    }

    @Override
    public void er$setDamageScale(double scale){
        this.er$damageScale = scale ;
    }

    @Override
    public double er$getDamageScale(){
        return this.er$damageScale;
    }

    @Override
    public double er$getDamage(){
        return this.er$damage;
    }

    @Inject(method = "customArrow", at = @At("RETURN"))
    public void customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack, CallbackInfoReturnable<AbstractArrow> cir){
        if(this.er$getDamageScale() > 0 && arrow.getOwner() instanceof LivingEntity living && living.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            arrow.setBaseDamage(living.getAttributeValue(Attributes.ATTACK_DAMAGE) * this.er$getDamageScale());
        }
    }
}
