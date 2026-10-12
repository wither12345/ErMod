package net.wither.er.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.item.IProjectileWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public abstract class CrossBowItemMixin{
    @Inject(method = "shootProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private static void modifyArrowDamage(Level level, LivingEntity living, InteractionHand hand, ItemStack crossBow, ItemStack arrow, float v, boolean b, float v1, float v2, float v3, CallbackInfo ci, @Local(name = "projectile") Projectile projectile) {
        if(living.getAttribute(Attributes.ATTACK_DAMAGE) != null &&
                crossBow.getItem() instanceof IProjectileWeapon projectileWeapon &&
                projectileWeapon.er$getDamageScale() > 0 &&
                projectile instanceof AbstractArrow abstractArrow) {
            abstractArrow.setBaseDamage(living.getAttributeValue(Attributes.ATTACK_DAMAGE) * projectileWeapon.er$getDamageScale());
        }
    }
}