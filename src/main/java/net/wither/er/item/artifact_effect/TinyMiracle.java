package net.wither.er.item.artifact_effect;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import net.wither.er.init.EffectRegister;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.item.data.weapon.BeAttackedAbility;

import java.util.List;

public class TinyMiracle extends AttrArtifactEffect implements BeAttackedAbility {
    private static final AttributeModifier RES_MODIFIER =
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "tiny_miracle"), 20, AttributeModifier.Operation.ADD_VALUE);

    public TinyMiracle() {
        super(List.of(
                new AttributePair(ErAttributeRegister.ANEMO_RES, RES_MODIFIER),
                new AttributePair(ErAttributeRegister.CRYO_RES, RES_MODIFIER),
                new AttributePair(ErAttributeRegister.DENDRO_RES, RES_MODIFIER),
                new AttributePair(ErAttributeRegister.GEO_RES, RES_MODIFIER),
                new AttributePair(ErAttributeRegister.HYDRO_RES, RES_MODIFIER),
                new AttributePair(ErAttributeRegister.ELECTRO_RES, RES_MODIFIER),
                new AttributePair(ErAttributeRegister.PYRO_RES, RES_MODIFIER)
        ));
    }

    @Override
    public void beAttacked(LivingEntity self, DamageSource source, EntityHurtEvent.DamageModifier modifier, float damageAmount, int level) {
        if(level > 3 && !self.hasEffect(EffectRegister.TINY_MIRACLE) && source instanceof ElementSourceInterface sourceInterface){
            ElementSource elementSource = sourceInterface.er$getSource();
            if(elementSource != null){
                int index = elementSource.getCategory().ordinal();
                self.addEffect(new MobEffectInstance(EffectRegister.TINY_MIRACLE, 200, index));
            }
        }
    }
}
