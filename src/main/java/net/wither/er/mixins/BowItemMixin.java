package net.wither.er.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.item.IProjectileWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public abstract class BowItemMixin implements IProjectileWeapon {
    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    private void modifyArrowDamage(ItemStack itemStack, Level level, LivingEntity living, int t, CallbackInfo ci, @Local(name = "abstractarrow") AbstractArrow abstractarrow) {
        if(living.getAttribute(Attributes.ATTACK_DAMAGE) != null && this.er$getDamageScale() > 0)
            abstractarrow.setBaseDamage(living.getAttributeValue(Attributes.ATTACK_DAMAGE) * this.er$getDamageScale());
    }
}