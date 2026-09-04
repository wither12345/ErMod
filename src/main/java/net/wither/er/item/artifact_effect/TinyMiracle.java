package net.wither.er.item.artifact_effect;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.init.MobEffectRegister;
import net.wither.er.item.data.weapon.BeAttackedAbility;

import java.util.List;
import java.util.UUID;

public class TinyMiracle extends AttrArtifactEffect implements BeAttackedAbility {
    private static final AttributeModifier RES_MODIFIER =
            new AttributeModifier(UUID.fromString("AC279C34-79FA-07A0-A13E-34B76CC6385D"), "tiny_miracle", 20, AttributeModifier.Operation.ADDITION);

    public TinyMiracle() {
        super(List.of(
                new AttributePair(ErAttributeRegister.ANEMO_RES.get(), RES_MODIFIER),
                new AttributePair(ErAttributeRegister.CRYO_RES.get(), RES_MODIFIER),
                new AttributePair(ErAttributeRegister.DENDRO_RES.get(), RES_MODIFIER),
                new AttributePair(ErAttributeRegister.GEO_RES.get(), RES_MODIFIER),
                new AttributePair(ErAttributeRegister.HYDRO_RES.get(), RES_MODIFIER),
                new AttributePair(ErAttributeRegister.ELECTRO_RES.get(), RES_MODIFIER),
                new AttributePair(ErAttributeRegister.PYRO_RES.get(), RES_MODIFIER)
        ));
    }

    @Override
    public void beAttacked(LivingEntity self, DamageSource source, EntityHurtEvent.DamageModifier modifier, float damageAmount, int level) {
        if(level > 3 && !self.hasEffect(MobEffectRegister.TINY_MIRACLE.get()) && source instanceof ElementSourceInterface sourceInterface){
            ElementSource elementSource = sourceInterface.er$getSource();
            if(elementSource != null){
                int index = elementSource.getCategory().ordinal();
                self.addEffect(new MobEffectInstance(MobEffectRegister.TINY_MIRACLE.get(), 200, index));
            }
        }
    }
}
