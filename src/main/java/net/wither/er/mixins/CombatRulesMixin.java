package net.wither.er.mixins;

import net.mcreator.er.ERConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CombatRules.class)
public abstract class CombatRulesMixin {
	@Inject(method = "getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F", at = @At("HEAD"), cancellable = true)
	private static void getDamageAfterAbsorb(LivingEntity entity, float damage, DamageSource source, float armor, float toughness, CallbackInfoReturnable<Float> info) {
		if (ERConfig.ARMOR_RULE_MODIFY.get()) {
			ItemStack itemstack = source.getWeaponItem();
			if (itemstack != null && entity.level() instanceof ServerLevel serverlevel) {
				armor *= Mth.clamp(EnchantmentHelper.modifyArmorEffectiveness(serverlevel, itemstack, entity, source, 1), 0.0F, 1.0F);
			} else {
				armor = armor;
			}
			damage *= 100f / (armor + 100);
			if (damage >= entity.getMaxHealth() * 0.1f) {
				damage -= (damage - entity.getMaxHealth() * 0.1f) * toughness / (toughness + 4);
			}
			info.setReturnValue(damage);
		}
	}
}