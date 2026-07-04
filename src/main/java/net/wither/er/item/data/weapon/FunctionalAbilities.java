package net.wither.er.item.data.weapon;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.wither.er.elements.AuraContainer;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.init.EffectRegister;

public class FunctionalAbilities {
    public static void coolSteel(DamageSource source, LivingEntity entity, EntityHurtEvent.DamageModifier modifier, int level){
        if(entity instanceof AuraContainerInterface auraContainerInterface &&
                (auraContainerInterface.er$getAuraContainer().hasElementCategory(Element.Category.CRYO) ||
                auraContainerInterface.er$getAuraContainer().hasElementCategory(Element.Category.HYDRO))){
            modifier.common_multiply += 0.09f + 0.03f * level;
        }
    }

    public static void darkIronSword(AuraContainer container, ElementSource elementToAdd, Element.Category elementReacted, EntityHurtEvent.DamageModifier damageModifier, Entity applier, int level){
        if(applier instanceof LivingEntity livingEntity && (elementReacted == Element.Category.ELECTRO || elementToAdd.getCategory() == Element.Category.ELECTRO)){
            livingEntity.addEffect(new MobEffectInstance(EffectRegister.OVERLOADED, 240, level - 1));
        }
    }
}
