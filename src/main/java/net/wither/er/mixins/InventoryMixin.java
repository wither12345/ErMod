package net.wither.er.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.wither.er.item.data.weapon.WeaponLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(Inventory.class)
public class InventoryMixin {
    @WrapOperation(method = "hurtArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    public void wrapHurtAndBreak(ItemStack instance, int amount, LivingEntity living, Consumer<? extends LivingEntity> consumer, Operation<Void> original){
        LazyOptional<WeaponLevelData> data = instance.getCapability(WeaponLevelData.WEAPON_LEVEL);
        if(data.isPresent() && data.resolve().isPresent()){
            int level = data.resolve().get().level();
            original.call(instance, amount / (int) (1 + level / 10f), living, consumer);
        }
        else original.call(instance, amount, living, consumer);
    }
}
