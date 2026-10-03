package net.wither.er.mixins;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.init.EffectRegister;
import net.wither.er.item.data.ErFoodData;
import net.wither.er.item.data.ErFoodDataListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    public void finishInject(ItemStack stack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir){
        FoodProperties properties = stack.get(DataComponents.FOOD);
        if(livingEntity instanceof Player player && !player.getFoodData().needsFood() && properties != null && !properties.canAlwaysEat()){
            MobEffectInstance instance = livingEntity.getEffect(EffectRegister.APPETIZER);
            if(instance != null){
                MobEffectInstance newInstance = new MobEffectInstance(EffectRegister.APPETIZER, instance.getDuration(), instance.getAmplifier() - 1, instance.isAmbient(), instance.isVisible(), instance.showIcon());
                livingEntity.removeEffect(EffectRegister.APPETIZER);
                if(instance.getAmplifier() > 0)
                    livingEntity.addEffect(newInstance);
            }
        }

        ErFoodData data = ErFoodDataListener.getData(stack);
        if(data != null)
            data.applyTo(livingEntity);
    }
}
