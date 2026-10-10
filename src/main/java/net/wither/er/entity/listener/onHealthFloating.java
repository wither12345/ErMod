package net.wither.er.entity.listener;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.wither.er.entity.IErEntity;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.data.weapon.HealthFloatingAbility;
import net.wither.er.item.data.weapon.WeaponAbilityData;

public class onHealthFloating {
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
