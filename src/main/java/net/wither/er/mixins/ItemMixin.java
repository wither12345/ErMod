package net.wither.er.mixins;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.init.DataComponentsRegister;
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
        ErFoodData data = stack.get(DataComponentsRegister.ER_FOOD);
        if(data == null)
            data = ErFoodDataListener.getData(stack);
        if(data != null)
            data.applyTo(livingEntity);
    }
}
