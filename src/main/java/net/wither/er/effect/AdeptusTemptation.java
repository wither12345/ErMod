package net.wither.er.effect;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class AdeptusTemptation extends MobEffect {
    private static final ResourceLocation ADEPTUS = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "adeptus_temptation");
    public AdeptusTemptation() {
        super(MobEffectCategory.BENEFICIAL, 0xffffbb88);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap attributeMap, int amp) {
        AttributeInstance instanceDMG = attributeMap.getInstance(Attributes.ATTACK_DAMAGE);
        if(instanceDMG != null){
            instanceDMG.removeModifier(ADEPTUS);
            instanceDMG.addPermanentModifier(new AttributeModifier(ADEPTUS, 37 + amp * 8, AttributeModifier.Operation.ADD_VALUE));
        }
        AttributeInstance instanceRATE = attributeMap.getInstance(ErModAttributes.CRIT_RATE);
        if(instanceRATE != null){
            instanceRATE.removeModifier(ADEPTUS);
            instanceRATE.addPermanentModifier(new AttributeModifier(ADEPTUS, 0.09 + amp * 0.01, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull AttributeMap attributeMap) {
        AttributeInstance instanceDMG = attributeMap.getInstance(Attributes.ATTACK_DAMAGE);
        if(instanceDMG != null)
            instanceDMG.removeModifier(ADEPTUS);
        AttributeInstance instanceRATE = attributeMap.getInstance(ErModAttributes.CRIT_RATE);
        if(instanceRATE != null)
            instanceRATE.removeModifier(ADEPTUS);
    }

    @Override
    public void createModifiers(int amp, @NotNull BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        consumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(ADEPTUS, 37 + amp * 8, AttributeModifier.Operation.ADD_VALUE));
        consumer.accept(ErModAttributes.CRIT_RATE, new AttributeModifier(ADEPTUS, 0.09 + amp * 0.01, AttributeModifier.Operation.ADD_VALUE));
    }
}
