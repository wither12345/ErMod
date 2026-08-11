package net.wither.er.player;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.item.data.weapon.HealthFloatingAbility;
import net.wither.er.item.weapons.AbilityWeapon;

public class onHealthFloating {
    public static void onFloating(LivingEntity entity, float d){
        if(entity instanceof ErEntityInterface erEntityInterface){
            Object2IntMap<ArtifactEffect> map = erEntityInterface.er$getEffectMap();
            for(Object2IntMap.Entry<ArtifactEffect> effect : map.object2IntEntrySet()){
                if(effect.getKey() instanceof HealthFloatingAbility ability){
                    ability.onFloat(entity, d, effect.getIntValue());
                }
            }
        }

        if(entity.getMainHandItem().getItem() instanceof AbilityWeapon abilityWeapon && abilityWeapon.getAbility() instanceof HealthFloatingAbility ability){
            CompoundTag tag = entity.getMainHandItem().getOrCreateTag();
            int refinement = tag.contains("refinement") ? tag.getInt("refinement") : 1 ;
            ability.onFloat(entity, d, refinement);
        }
    }
}
