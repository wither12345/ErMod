package net.wither.er.entity.outcrop;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeGiver extends EntityModifier{
    private final Attribute attribute;
    private final AttributeModifier modifier;

    public AttributeGiver(Attribute attribute, AttributeModifier modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public static AttributeGiver read(JsonElement element) {
        ResourceLocation attrLocation = new ResourceLocation(element.getAsJsonObject().get("attribute").getAsString()) ;
        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(attrLocation);
        AttributeModifier modifier = AttributeModifier.load(CompoundTag.CODEC.parse(JsonOps.INSTANCE, element.getAsJsonObject().get("modifier")).result().get());
        return new AttributeGiver(attribute, modifier);
    }

    @Override
    public void apply(Entity entity, int level) {
        if(entity instanceof LivingEntity living){
            AttributeInstance instance = living.getAttribute(attribute);
            if(instance != null)
                instance.addPermanentModifier(modifier);
        }
    }
}
