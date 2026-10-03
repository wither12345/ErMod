package net.wither.er.mixins;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.init.MobEffectRegister;
import net.wither.er.item.data.ErFoodData;
import net.wither.er.item.data.ErFoodDataListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Item.class)
public class ItemMixin {
    @Shadow @Final @Nullable private FoodProperties foodProperties;

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    public void finishInject(ItemStack stack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir){
        if(this.foodProperties != null && livingEntity instanceof Player player && !player.getFoodData().needsFood() && !foodProperties.canAlwaysEat()){
            MobEffectInstance instance = livingEntity.getEffect(MobEffectRegister.APPETIZER.get());
            if(instance != null){
                MobEffectInstance newInstance = new MobEffectInstance(MobEffectRegister.APPETIZER.get(), instance.getDuration(), instance.getAmplifier() - 1, instance.isAmbient(), instance.isVisible(), instance.showIcon());
                livingEntity.removeEffect(MobEffectRegister.APPETIZER.get());
                if(instance.getAmplifier() > 0)
                    livingEntity.addEffect(newInstance);
            }
        }

        ErFoodData data = ErFoodDataListener.getData(stack);
        if(data != null)
            data.applyTo(livingEntity);
    }
}
