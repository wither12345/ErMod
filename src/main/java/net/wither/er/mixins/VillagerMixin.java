package net.wither.er.mixins;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mcreator.er.init.ErModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {
    @Shadow
    public abstract GossipContainer getGossips();

    @Shadow
    public abstract int getPlayerReputation(Player p_35533_);

    @Unique private static final AttributeModifier ER$MODIFIER =
        new AttributeModifier(UUID.fromString("D66DD5B3-374B-D680-3A34-443AA91B46FB"), "er:lantern_of_life", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);

    protected VillagerMixin(EntityType<? extends AbstractVillager> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void mobInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        AttributeInstance instance = this.getAttribute(Attributes.MAX_HEALTH);
        ItemStack itemStack = player.getMainHandItem();
        if(itemStack.is(ErModItems.LANTERN_OF_LIFE.get()) && instance != null && !instance.hasModifier(ER$MODIFIER)){
            ItemStack item = player.getItemInHand(InteractionHand.OFF_HAND);
            int ems_cost = Math.max(5, 32 - 2 * this.getPlayerReputation(player));
            if(item.is(Items.EMERALD) && item.getCount() >= ems_cost){
                itemStack.hurtAndBreak(1, player, null);
                instance.addPermanentModifier(ER$MODIFIER);
                item.shrink(ems_cost);
                player.addItem(new ItemStack(ErModItems.SEAL_OF_LIFE.get()));
                GossipType gossipToRemove = null;
                Object2IntMap<GossipType> removingMap = null;
                int val = 0;
                this.getPlayerReputation(player);
                for(Object2IntMap<GossipType> map: this.getGossips().getGossipEntries().values()){
                    for(Object2IntMap.Entry<GossipType> gossip : map.object2IntEntrySet()){
                        if(gossip.getIntValue() > val) {
                            gossipToRemove = gossip.getKey();
                            removingMap = map;
                            val = gossip.getIntValue();
                        }
                    }
                }
                if(gossipToRemove != null){
                    removingMap.remove(gossipToRemove, val);
                }
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
