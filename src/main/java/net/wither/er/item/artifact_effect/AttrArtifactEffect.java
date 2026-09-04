package net.wither.er.item.artifact_effect;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AttrArtifactEffect extends ArtifactEffect{
    private final List<AttributePair> pairs;
    public AttrArtifactEffect(Holder<Attribute> attr, AttributeModifier modifier) {
        this(List.of(new AttributePair(attr, modifier)));
    }

    public AttrArtifactEffect(List<AttributePair> pairs){
        this.pairs = pairs;
    }

    public void addAttributeModifiers(@NotNull AttributeMap map, int amp) {
        if(amp > 1)
            for(AttributePair pair : pairs)
                pair.apply(map);
    }

    public void removeAttributeModifiers(AttributeMap map) {
        for(AttributePair pair : pairs)
            pair.remove(map);
    }

    public record AttributePair(Holder<Attribute> attr, AttributeModifier modifier){
        public void apply(AttributeMap map){
            AttributeInstance attributeinstance = map.getInstance(attr);
            if (attributeinstance != null)
                attributeinstance.addOrUpdateTransientModifier(modifier);
        }

        public void remove(AttributeMap map){
            AttributeInstance attributeinstance = map.getInstance(attr);
            if (attributeinstance != null)
                attributeinstance.removeModifier(modifier);
        }
    }
}
