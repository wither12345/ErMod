package net.wither.er.effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AdeptusTemptation extends MobEffect {
    private static final UUID ADEPTUS = UUID.fromString("26FB5933-DE05-0A2E-8ABF-CD8D77BCE160");
    public AdeptusTemptation() {
        super(MobEffectCategory.BENEFICIAL, 0xffffbb88);
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity livingEntity, @NotNull AttributeMap attributeMap, int amp) {
        AttributeInstance instanceDMG = attributeMap.getInstance(Attributes.ATTACK_DAMAGE);
        if(instanceDMG != null){
            instanceDMG.removeModifier(ADEPTUS);
            instanceDMG.addPermanentModifier(new AttributeModifier(ADEPTUS, "adeptus_temptation", 37 + amp * 8, AttributeModifier.Operation.ADDITION));
        }
        AttributeInstance instanceRATE = attributeMap.getInstance(ErModAttributes.CRIT_RATE.get());
        if(instanceRATE != null){
            instanceRATE.removeModifier(ADEPTUS);
            instanceRATE.addPermanentModifier(new AttributeModifier(ADEPTUS, "adeptus_temptation", 0.09 + amp * 0.01, AttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity livingEntity, @NotNull AttributeMap attributeMap, int amp) {
        AttributeInstance instanceDMG = attributeMap.getInstance(Attributes.ATTACK_DAMAGE);
        if(instanceDMG != null)
            instanceDMG.removeModifier(ADEPTUS);
        AttributeInstance instanceRATE = attributeMap.getInstance(ErModAttributes.CRIT_RATE.get());
        if(instanceRATE != null)
            instanceRATE.removeModifier(ADEPTUS);
    }
}
