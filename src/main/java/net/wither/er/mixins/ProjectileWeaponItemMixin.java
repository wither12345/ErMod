package net.wither.er.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.wither.er.item.BowInterface;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

import static net.minecraft.world.item.Item.BASE_ATTACK_DAMAGE_ID;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin implements IItemExtension , BowInterface {
    @Unique double Damage = 4 ;

    @Override
    public void setDamage(double damage){
        this.Damage = damage ;
    }

    @Override
    public double getDamage(){return this.Damage ;}

    @Inject(method = "createProjectile" , at = @At("RETURN"),locals = LocalCapture.CAPTURE_FAILSOFT)
    public void createProjectile(Level level, LivingEntity entity, ItemStack weaponItem, ItemStack arrowItem, boolean critical, CallbackInfoReturnable<Projectile> ci, @Local(ordinal = 0) AbstractArrow abstractArrow){
        abstractArrow.setBaseDamage(entity.getAttributeValue(Attributes.ATTACK_DAMAGE) / 3);
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack){
        return new ItemAttributeModifiers(List.of(new ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE , new AttributeModifier(BASE_ATTACK_DAMAGE_ID, Damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)),true);
    }
}
