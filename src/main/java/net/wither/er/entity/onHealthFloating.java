package net.wither.er.entity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.weapon.HealthFloatingAbility;
import net.wither.er.item.data.weapon.WeaponRefinement;

public class onHealthFloating {
    public static void onFloating(LivingEntity entity, float d){
        if(entity instanceof ErEntityInterface erEntityInterface){
            Object2IntMap<Holder<ArtifactEffect>> map = erEntityInterface.er$getEffectMap();
            for(Object2IntMap.Entry<Holder<ArtifactEffect>> effect : map.object2IntEntrySet()){
                if(effect.getKey().value() instanceof HealthFloatingAbility ability){
                    ability.onFloat(entity, d, effect.getIntValue());
                }
            }
        }
        WeaponRefinement refinement = entity.getMainHandItem().get(DataComponentsRegister.WEAPON_REFINEMENT.get());
        if(refinement != null && refinement.getAbility() instanceof HealthFloatingAbility ability){
            ability.onFloat(entity, d, refinement.refineLevel());
        }
    }
}
