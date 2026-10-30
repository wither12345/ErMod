package net.wither.er.effect;

import net.mcreator.er.ErMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.init.ErAttributeRegister;
import org.jetbrains.annotations.NotNull;

public class GustSurgeReduceRES extends MobEffect {
    private static final AttributeModifier MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "gust_surge"), -20, AttributeModifier.Operation.ADD_VALUE);

    public GustSurgeReduceRES() {
        super(MobEffectCategory.HARMFUL, 0xFF44FF99);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap attributeMap, int amp) {
        AttributeInstance anemo = attributeMap.getInstance(ErAttributeRegister.ANEMO_RES);
        if(anemo != null) 
            anemo.addOrReplacePermanentModifier(MODIFIER);

        AttributeInstance electro = attributeMap.getInstance(ErAttributeRegister.ELECTRO_RES);
        if(electro != null && amp == 1)
            electro.addOrReplacePermanentModifier(MODIFIER);

        AttributeInstance hydro = attributeMap.getInstance(ErAttributeRegister.HYDRO_RES);
        if(hydro != null && amp == 2)
            hydro.addOrReplacePermanentModifier(MODIFIER);

        AttributeInstance pyro = attributeMap.getInstance(ErAttributeRegister.PYRO_RES);
        if(pyro != null && amp == 3)
            pyro.addOrReplacePermanentModifier(MODIFIER);

        AttributeInstance cryo = attributeMap.getInstance(ErAttributeRegister.CRYO_RES);
        if(cryo != null && amp == 4)
            cryo.addOrReplacePermanentModifier(MODIFIER);
    }

    @Override
    public void removeAttributeModifiers(@NotNull AttributeMap attributeMap) {
        AttributeInstance anemo = attributeMap.getInstance(ErAttributeRegister.ANEMO_RES);
        if(anemo != null)
            anemo.removeModifier(MODIFIER);

        AttributeInstance electro = attributeMap.getInstance(ErAttributeRegister.ELECTRO_RES);
        if(electro != null)
            electro.removeModifier(MODIFIER);

        AttributeInstance hydro = attributeMap.getInstance(ErAttributeRegister.HYDRO_RES);
        if(hydro != null)
            hydro.removeModifier(MODIFIER);

        AttributeInstance pyro = attributeMap.getInstance(ErAttributeRegister.PYRO_RES);
        if(pyro != null)
            pyro.removeModifier(MODIFIER);

        AttributeInstance cryo = attributeMap.getInstance(ErAttributeRegister.CRYO_RES);
        if(cryo != null)
            cryo.removeModifier(MODIFIER);
    }
}
