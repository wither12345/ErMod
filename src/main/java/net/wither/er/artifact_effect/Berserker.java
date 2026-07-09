package net.wither.er.artifact_effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.item.data.weapon.HealthFloatingAbility;

public class Berserker extends TwoSetAttrEffect implements HealthFloatingAbility {
    private static final AttributeModifier MODIFIER_LOW = new AttributeModifier(ResourceLocation.parse("er:berserker_low_hp"), 0.24, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier MODIFIER = new AttributeModifier(ResourceLocation.parse("er:berserker"), 0.12, AttributeModifier.Operation.ADD_VALUE);

    public Berserker() {
        super(ErModAttributes.CRIT_RATE, MODIFIER);
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        super.removeAttributeModifiers(map);
        AttributeInstance instance = map.getInstance(ErModAttributes.CRIT_RATE);
        if(instance == null) return;
        instance.removeModifier(MODIFIER_LOW);
    }

    @Override
    public void onFloat(LivingEntity entity, float delta, int level) {
        if(level > 3){
            AttributeInstance instance = entity.getAttribute(ErModAttributes.CRIT_RATE);
            if(instance == null) return;
            if (entity.getHealth() <= entity.getMaxHealth() * 0.7)
                instance.addTransientModifier(MODIFIER_LOW);
        }
    }
}
