package net.wither.er.item.data.weapon;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;

public class CoolSteelAbility {

    public static void modify(DamageSource source, LivingEntity entity, EntityHurtEvent.DamageModifier modifier, int level){
        if(entity instanceof AuraContainerInterface auraContainerInterface &&
                (auraContainerInterface.er$getAuraContainer().hasElementCategory(Element.Category.CRYO) ||
                auraContainerInterface.er$getAuraContainer().hasElementCategory(Element.Category.HYDRO))){
            modifier.common_multiply += 0.09f + 0.03f * level;
        }
    }
}
