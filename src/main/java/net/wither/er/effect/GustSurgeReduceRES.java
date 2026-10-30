package net.wither.er.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.init.ErAttributeRegister;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GustSurgeReduceRES extends MobEffect {
    private static final AttributeModifier MODIFIER = new AttributeModifier(UUID.fromString("3F07C362-A013-8AF5-46E6-1AD9EBB9D90A"), "gust_surge", -20,  AttributeModifier.Operation.ADDITION);

    public GustSurgeReduceRES() {
        super(MobEffectCategory.HARMFUL, 0xFF44FF99);
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity living, @NotNull AttributeMap attributeMap, int amp) {
        AttributeInstance anemo = attributeMap.getInstance(ErAttributeRegister.ANEMO_RES.get());
        if(anemo != null) {
            anemo.removeModifier(MODIFIER);
            anemo.addPermanentModifier(MODIFIER);
        }

        AttributeInstance electro = attributeMap.getInstance(ErAttributeRegister.ELECTRO_RES.get());
        if(electro != null && amp == 1) {
            electro.removeModifier(MODIFIER);
            electro.addPermanentModifier(MODIFIER);
        }

        AttributeInstance hydro = attributeMap.getInstance(ErAttributeRegister.HYDRO_RES.get());
        if(hydro != null && amp == 2) {
            hydro.removeModifier(MODIFIER);
            hydro.addPermanentModifier(MODIFIER);
        }

        AttributeInstance pyro = attributeMap.getInstance(ErAttributeRegister.PYRO_RES.get());
        if(pyro != null && amp == 3) {
            pyro.removeModifier(MODIFIER);
            pyro.addPermanentModifier(MODIFIER);
        }

        AttributeInstance cryo = attributeMap.getInstance(ErAttributeRegister.CRYO_RES.get());
        if(cryo != null && amp == 4) {
            cryo.removeModifier(MODIFIER);
            cryo.addPermanentModifier(MODIFIER);
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity living, @NotNull AttributeMap attributeMap, int amp) {
        AttributeInstance anemo = attributeMap.getInstance(ErAttributeRegister.ANEMO_RES.get());
        if(anemo != null)
            anemo.removeModifier(MODIFIER);

        AttributeInstance electro = attributeMap.getInstance(ErAttributeRegister.ELECTRO_RES.get());
        if(electro != null)
            electro.removeModifier(MODIFIER);

        AttributeInstance hydro = attributeMap.getInstance(ErAttributeRegister.HYDRO_RES.get());
        if(hydro != null)
            hydro.removeModifier(MODIFIER);

        AttributeInstance pyro = attributeMap.getInstance(ErAttributeRegister.PYRO_RES.get());
        if(pyro != null)
            pyro.removeModifier(MODIFIER);

        AttributeInstance cryo = attributeMap.getInstance(ErAttributeRegister.CRYO_RES.get());
        if(cryo != null)
            cryo.removeModifier(MODIFIER);
    }
}
