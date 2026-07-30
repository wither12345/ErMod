package net.wither.er.artifact_effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.item.data.weapon.HealthFloatingAbility;

import java.util.UUID;

public class Berserker extends TwoSetAttrEffect implements HealthFloatingAbility {
    private static final String BERSERKER_LOW = "er:berserker_low_hp";
    private static final UUID BERSERKER = UUID.fromString("5F92757D-9E1B-8DA1-B0D0-E656B4F5416D");
    private static final AttributeModifier MODIFIER = new AttributeModifier(BERSERKER, BERSERKER_LOW, 0.24, AttributeModifier.Operation.ADDITION);

    public Berserker() {
        super(ErModAttributes.CRIT_RATE.get(), new AttributeModifier(UUID.fromString("C90A991A-97E2-B100-76C5-AF332D025984"), "berserker" , 0.12, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        super.removeAttributeModifiers(map);
        AttributeInstance instance = map.getInstance(ErModAttributes.CRIT_RATE.get());
        if(instance == null) return;
        instance.removeModifier(BERSERKER);
    }

    @Override
    public void onFloat(LivingEntity entity, float delta, int level) {
        if(level > 3 && entity instanceof ErEntityInterface erEntityInterface){
            AttributeInstance instance = entity.getAttribute(ErModAttributes.CRIT_RATE.get());
            if(instance == null) return;
            instance.removeModifier(BERSERKER);
            if (erEntityInterface.er$getArtifactEffectLevel(ArtifactEffectRegistry.BERSERKER.get()) > 3) {
                if (entity.getHealth() <= entity.getMaxHealth() * 0.7 && !instance.hasModifier(MODIFIER))
                    instance.addTransientModifier(MODIFIER);
            }
        }
    }
}
