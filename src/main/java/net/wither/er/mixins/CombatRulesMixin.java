package net.wither.er.mixins;

import net.mcreator.er.ERConfig;
import net.minecraft.world.damagesource.CombatRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CombatRules.class)
public abstract class CombatRulesMixin {
	@Inject(method = "getDamageAfterAbsorb", at = @At("HEAD"), cancellable = true)
	private static void getDamageAfterAbsorb(float damage, float armor, float toughness, CallbackInfoReturnable<Float> info) {
		if (ERConfig.ARMOR_RULE_MODIFY.get()) {
			damage *= 100f / (armor* (toughness/ 4 + 1) + 100);
			info.setReturnValue(damage);
		}
	}
}