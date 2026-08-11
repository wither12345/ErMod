package net.wither.er.item.artifact_effect;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class TwoSetAttrEffect extends ArtifactEffect{
    private final Holder<Attribute> attr;
    private final AttributeModifier modifier;
    public TwoSetAttrEffect(Holder<Attribute> attr, AttributeModifier modifier) {
        super();
        this.modifier = modifier;
        this.attr = attr;
    }

    public void addAttributeModifiers(@NotNull AttributeMap map, int amp) {
        if(amp > 1){
            AttributeInstance attributeinstance = map.getInstance(attr);
            if (attributeinstance != null) {
                attributeinstance.removeModifier(modifier);
                attributeinstance.addPermanentModifier(modifier);
            }
        }
    }

    public void removeAttributeModifiers(AttributeMap map) {
        AttributeInstance attributeinstance = map.getInstance(attr);
        if (attributeinstance != null) {
            attributeinstance.removeModifier(modifier);
        }
    }
}
