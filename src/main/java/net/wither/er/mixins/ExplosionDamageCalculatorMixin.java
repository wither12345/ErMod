package net.wither.er.mixins;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExplosionDamageCalculator.class)
public class ExplosionDamageCalculatorMixin {
    @Inject(method = "getEntityDamageAmount(Lnet/minecraft/world/level/Explosion;Lnet/minecraft/world/entity/Entity;)F", at = @At("RETURN"), cancellable = true)
    private void getEntityDamageAmount(Explosion explosion, Entity entity, CallbackInfoReturnable<Float> info) {
        if(explosion.getIndirectSourceEntity() != null) {
            if(info == null)
                return;
            int level = EntityHurtEvent.getEntityLevel(explosion.getIndirectSourceEntity());
            info.setReturnValue(info.getReturnValue() * EntityHurtEvent.getLevelMultiply(level));
        }
    }
}
