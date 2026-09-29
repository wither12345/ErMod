package net.wither.er.mixins;

import net.mcreator.er.init.ErModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin extends AbstractVillager {
    @Unique private static final AttributeModifier ER$MODIFIER =
            new AttributeModifier(UUID.fromString("D66DD5B3-374B-D680-3A34-443AA91B46FB"), "er:lantern_of_life", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);

    protected WanderingTraderMixin(EntityType<? extends AbstractVillager> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void mobInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        AttributeInstance instance = this.getAttribute(Attributes.MAX_HEALTH);
        ItemStack itemStack = player.getMainHandItem();
        if(itemStack.is(ErModItems.LANTERN_OF_LIFE.get()) && instance != null && !instance.hasModifier(ER$MODIFIER)){
            InteractionHand otherHand = InteractionHand.OFF_HAND;
            ItemStack item = player.getItemInHand(otherHand);
            if(item.is(Items.EMERALD) && item.getCount() >= 32){
                itemStack.hurtAndBreak(1, player, null);
                instance.addPermanentModifier(ER$MODIFIER);
                item.shrink(32);
                player.addItem(new ItemStack(ErModItems.SEAL_OF_LIFE.get()));
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
