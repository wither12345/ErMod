package net.wither.er.entity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.init.ErModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.data.weapon.HealthFloatingAbility;
import net.wither.er.item.data.weapon.TickAbility;
import net.wither.er.item.data.weapon.WeaponAbilityData;

@Mod.EventBusSubscriber
public class OnEvents {
    @SubscribeEvent
    public static void onHeal(LivingHealEvent event){
        LivingEntity entity = event.getEntity();
        if(entity.getAttributes().hasAttribute(ErModAttributes.INCOMING_HEALING_BONUS.get()))
            event.setAmount(event.getAmount() * (float)entity.getAttributeValue(ErModAttributes.INCOMING_HEALING_BONUS.get()));
    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event){
        LivingEntity entity = event.getEntity();
        if(entity instanceof IErEntity erEntity){
            Object2IntMap<ArtifactEffect> map = erEntity.er$getEffectMap();
            for(Object2IntMap.Entry<ArtifactEffect> effect : map.object2IntEntrySet()){
                if(effect.getKey() instanceof TickAbility ability){
                    ability.tick(entity, effect.getIntValue());
                }
            }
        }
        ItemStack handItem = entity.getMainHandItem();
        handItem.getCapability(WeaponAbilityData.WEAPON_ABILITY).ifPresent(weaponAbility -> {
            if (weaponAbility.ability().get() instanceof TickAbility ability) {
                CompoundTag tag = handItem.getOrCreateTag();
                int refinement = tag.contains("refinement") ? tag.getInt("refinement") : 1;
                ability.tick(entity, refinement);
            }
        });
    }

    @SubscribeEvent
    public static void onDropItem(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity instanceof Frog){
            event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ErModItems.FROG.get())));
        }
    }

    public static void onFloating(LivingEntity entity, float d){
        if(entity instanceof IErEntity erEntity){
            Object2IntMap<ArtifactEffect> map = erEntity.er$getEffectMap();
            for(Object2IntMap.Entry<ArtifactEffect> effect : map.object2IntEntrySet()){
                if(effect.getKey() instanceof HealthFloatingAbility ability){
                    ability.onFloat(entity, d, effect.getIntValue());
                }
            }
        }
        ItemStack handItem = entity.getMainHandItem();
        handItem.getCapability(WeaponAbilityData.WEAPON_ABILITY).ifPresent(weaponAbility -> {
            if (weaponAbility.ability().get() instanceof HealthFloatingAbility ability) {
                CompoundTag tag = handItem.getOrCreateTag();
                int refinement = tag.contains("refinement") ? tag.getInt("refinement") : 1;
                ability.onFloat(entity, d, refinement);
            }
        });
    }
}
